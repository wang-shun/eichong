package com.wanma.ims.service.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.wanma.ims.common.domain.CompanyDO;
import com.wanma.ims.common.domain.FinAccountDO;
import com.wanma.ims.common.domain.FinAccountRelationDO;
import com.wanma.ims.common.domain.UserCardDO;
import com.wanma.ims.common.domain.UserCompanyDO;
import com.wanma.ims.common.domain.UserDO;
import com.wanma.ims.common.domain.UserNormalDO;
import com.wanma.ims.common.dto.BaseResultDTO;
import com.wanma.ims.constants.ResultCodeConstants;
import com.wanma.ims.mapper.CompanyMapper;
import com.wanma.ims.mapper.FinAccountMapper;
import com.wanma.ims.mapper.FinAccountRelationMapper;
import com.wanma.ims.mapper.UserCardMapper;
import com.wanma.ims.mapper.UserCompanyMapper;
import com.wanma.ims.mapper.UserMapper;
import com.wanma.ims.mapper.UserNormalMapper;
import com.wanma.ims.service.FinAccountRelationService;
import com.wanma.ims.util.DateUtil;

@Service("finAccountRelationService")
public class FinAccountRelationServiceImpl implements FinAccountRelationService {

    private final Logger log = LoggerFactory.getLogger(this.getClass());
    @Autowired
    private FinAccountRelationMapper finAccountRelationMapper;

    @Autowired
    private CompanyMapper companyMapper;

    @Autowired
    private UserMapper userMapper;

    @Autowired
    private UserNormalMapper userNormalMapper;

    @Autowired
    private UserCompanyMapper userCompanyMapper;

    @Autowired
    private FinAccountMapper finAccountMapper;

    @Autowired
    private UserCardMapper userCardMapper;

    @Override
    public Long getFinAccountRelationCount(FinAccountRelationDO finAccountRelation) {
        return finAccountRelationMapper.getFinAccountRelationCount(finAccountRelation);
    }

    @Override
    public List<FinAccountRelationDO> getFinAccountRelationList(FinAccountRelationDO finAccountRelation) {
        return finAccountRelationMapper.getFinAccountRelationList(finAccountRelation);
    }

    @Override
    @Transactional
    public BaseResultDTO addFinAccountRelation(FinAccountRelationDO finAccountRelation, UserDO user) throws Exception {
        BaseResultDTO baseResultDTO = new BaseResultDTO();

        String userAccount = finAccountRelation.getUserAccount();
        if (userAccount == null || "".equals(userAccount)) {
            log.error(this.getClass() + ".addFinAccountRelation() error : 用户账号不能为空！");
            baseResultDTO.setSuccess(false);
            baseResultDTO.setErrorDetail("用户账号不能为空！");
            return baseResultDTO;
        }

        CompanyDO company = companyMapper.selectCompanyByCpyNumber(finAccountRelation.getCpyNumber());
        if (company == null || company.getCpyId() == null) {
            log.error(this.getClass() + ".addFinAccountRelation() error : 公司标识不存在！");
            baseResultDTO.setSuccess(false);
            baseResultDTO.setErrorDetail("公司标识不存在！");
            return baseResultDTO;
        }

        String accountNO = finAccountRelation.getAccountNO();
        if (accountNO == null || "".equals(accountNO)) {
            log.error(this.getClass() + ".addFinAccountRelation() error : 资金账号不能为空！");
            baseResultDTO.setSuccess(false);
            baseResultDTO.setErrorDetail("资金账号不能为空！");
            return baseResultDTO;
        }

        UserDO userDO = userMapper.selectUserByUserAccountAndCpyId(userAccount, company.getCpyId());
        if (userDO == null) {
            log.error(this.getClass() + ".addFinAccountRelation() error : 用户账号不存在！");
            baseResultDTO.setSuccess(false);
            baseResultDTO.setErrorDetail("用户账号不存在！");
            return baseResultDTO;
        } else {
            finAccountRelation.setUserId(userDO.getUserId());
        }

        FinAccountDO finAccount = new FinAccountDO();
        finAccount.setAccountNO(accountNO);
        List<FinAccountDO> qryAccountDOList = finAccountMapper.getFinAccountList(finAccount);
        if (qryAccountDOList == null || qryAccountDOList.size() < 1) {
            log.error(this.getClass() + ".addFinAccountRelation() error : 资金账户不存在！");
            baseResultDTO.setSuccess(false);
            baseResultDTO.setErrorDetail("资金账户不存在！");
            return baseResultDTO;
        } else {
            finAccountRelation.setAccountId(((FinAccountDO) qryAccountDOList.get(0)).getAccountId());
        }

        FinAccountRelationDO qryDO = new FinAccountRelationDO();
        qryDO.setBillAccountId(finAccountRelation.getBillAccountId());
        qryDO.setUserId(finAccountRelation.getUserId());
        qryDO.setAccountId(finAccountRelation.getAccountId());
        qryDO.setIsDel(0);
        Long count = finAccountRelationMapper.getFinAccountRelationCount(qryDO);
        if (count > 0) {
            log.error(this.getClass() + ".addFinAccountRelation() error : 账务关系已存在！");
            baseResultDTO.setSuccess(false);
            baseResultDTO.setErrorDetail("账务关系已存在！");
            return baseResultDTO;
        }

        //将该用户下相同账单科目记录的优先级置成不是优先
        if (finAccountRelation.getPriority().intValue() == 1) {
            qryDO = new FinAccountRelationDO();
            qryDO.setUserId(finAccountRelation.getUserId());
            qryDO.setBillAccountId(finAccountRelation.getBillAccountId());
            qryDO.setPriority(2);
            finAccountRelationMapper.modifyFinAccountRelation(qryDO);
        }

        if (finAccountRelation.getBillAccountId() == 1L) {
            UserDO qryUserDO = new UserDO();
            qryUserDO.setUserId(userDO.getUserId());
            List<UserDO> userList = userMapper.selectUserList(qryUserDO);
            if (userList == null || userList.size() < 1) {
                UserCardDO userCardDO = new UserCardDO();
                userCardDO.setUcUserId(userDO.getUserId());
                List<UserCardDO> list = userCardMapper.getCardList(userCardDO);
                if (list == null) {
                    log.error(this.getClass() + ".addFinAccountRelation() error : 用户不存在！");
                    baseResultDTO.setSuccess(false);
                    baseResultDTO.setErrorDetail("用户不存在！");
                    return baseResultDTO;
                } else {
                    userCardDO = list.get(0);
                    userCardDO.setAccountId(finAccountRelation.getAccountId());
                    userCardDO.setModifier(user.getUserId().toString());
                    userCardMapper.updateUserCard(userCardDO);
                }
            } else {
                userDO = userList.get(0);

                if (userDO.getCpyId().intValue() == 276) {//爱充
                    UserNormalDO userNormal = new UserNormalDO();
                    userNormal.setUserId(userDO.getUserId());
                    userNormal.setAccountId(finAccountRelation.getAccountId());
                    userNormalMapper.updateByUserIdSelective(userNormal);
                } else {
                    //公司
                    UserCompanyDO userCompany = new UserCompanyDO();
                    userCompany.setUserId(userDO.getUserId());
                    userCompany.setAccountId(finAccountRelation.getAccountId());
                    userCompanyMapper.updateByUserIdSelective(userCompany);
                }
            }
        }

        finAccountRelation.setIsDel(0);
        finAccountRelation.setCreator(user.getUserAccount());
        finAccountRelation.setModifier(user.getUserAccount());
        // 取系统当前时间
        Date now = DateUtil.parse(DateUtil.getNow(DateUtil.TYPE_COM_YMDHMS));
        finAccountRelation.setGmtCreate(now);
        finAccountRelation.setGmtModified(now);

        int result = finAccountRelationMapper.addFinAccountRelation(finAccountRelation);

        if (result == 0) {
            log.error(this.getClass() + ".addFinAccountRelation() error : " + ResultCodeConstants.ERROR_MSG_ERROR_ADD);
            baseResultDTO.setSuccess(false);
            baseResultDTO.setErrorDetail(ResultCodeConstants.ERROR_MSG_ERROR_ADD);
            return baseResultDTO;
        } else {
            return new BaseResultDTO();
        }
    }

    @Override
    @Transactional
    public int modifyFinAccountRelation(FinAccountRelationDO finAccountRelation, UserDO user) throws Exception {
        FinAccountRelationDO qryfinAccountRelation = new FinAccountRelationDO();
        qryfinAccountRelation.setPkId(finAccountRelation.getPkId());
        Long qryResult = finAccountRelationMapper.getFinAccountRelationCount(qryfinAccountRelation);
        if (qryResult < 1) {
            return 2;
        }

        finAccountRelation.setModifier(user.getUserAccount());
        finAccountRelation.setGmtModified(DateUtil.parse(DateUtil.getNow(DateUtil.TYPE_COM_YMDHMS)));

        return finAccountRelationMapper.modifyFinAccountRelation(finAccountRelation);
    }

    @Override
    @Transactional
    public BaseResultDTO removeFinAccountRelation(FinAccountRelationDO finAccountRelation) throws Exception {
        BaseResultDTO baseResultDTO = new BaseResultDTO();

        //如果账单科目是充电消费，需要处理
        if (finAccountRelation.getBillAccountId() != null && finAccountRelation.getBillAccountId().intValue() == 1) {
            FinAccountRelationDO qryDO = new FinAccountRelationDO();
            qryDO.setUserId(finAccountRelation.getUserId());
            qryDO.setBillAccountId(finAccountRelation.getBillAccountId());
            Long count = finAccountRelationMapper.getFinAccountRelationCount(qryDO);
            if (count != null && count.equals(1L)) {
                log.error(this.getClass() + ".removeFinAccountRelation() error : 充电消费的账单科目不允许删除！");
                baseResultDTO.setSuccess(false);
                baseResultDTO.setErrorDetail("充电消费的账单科目不允许删除！");
                return baseResultDTO;
            }
        }

        finAccountRelationMapper.removeFinAccountRelation(finAccountRelation);

        return baseResultDTO;
    }

    @Override
    public List<FinAccountRelationDO> getFinAccountRelation4User(UserNormalDO userNormal) {
        if (userNormal == null || userNormal.getUserId() == null) {
            log.error(this.getClass() + ".getFinAccountRelation4User() error : 用户信息不允许为空！");
            return null;
        }

        List<FinAccountRelationDO> list = finAccountRelationMapper.getFinAccountRelation4User(userNormal);

        return splitResult(list);
    }

    @Override
    public List<FinAccountRelationDO> getFinAccountRelation4Card(UserCardDO userCard) {
        if (userCard == null || userCard.getUcId() == null) {
            log.error(this.getClass() + ".getFinAccountRelation4Card() error : 卡信息不允许为空！");
            return null;
        }

        List<FinAccountRelationDO> list = finAccountRelationMapper.getFinAccountRelation4Card(userCard);

        return splitResult(list);
    }

    @Override
    public List<FinAccountRelationDO> getFinAccountRelation4Cpy(String accountId) {
        if (accountId == null || "".equals(accountId)) {
            log.error(this.getClass() + ".getFinAccountRelation4Cpy() error : 资金账户信息不允许为空！");
            return null;
        }

        List<FinAccountRelationDO> list = finAccountRelationMapper.getFinAccountRelation4Cpy(accountId);

        return splitResult(list);
    }

    /**
     * <p>Description: 拼接结果集</p>
     *
     * @param
     * @author bingo
     * @date 2017-7-10
     */
    private List<FinAccountRelationDO> splitResult(List<FinAccountRelationDO> list) {
        List<FinAccountRelationDO> resultList = new ArrayList<FinAccountRelationDO>();
        if (list != null && list.size() > 0) {
            StringBuffer strBuf = new StringBuffer();
            String accountNO = "";
            String billAccountName = "";
            todo:
            for (int i = 0; i < list.size(); i++) {
                accountNO = list.get(i).getAccountNO();
                billAccountName = list.get(i).getBillAccountName();
                strBuf.append(billAccountName).append("/");

                for (FinAccountRelationDO finAccountRelationDO : resultList) {
                    if (finAccountRelationDO.getAccountNO().equals(accountNO)) {
                        continue todo;
                    }
                }

                for (int j = i + 1; j < list.size(); j++) {
                    if (accountNO.equals(list.get(j).getAccountNO())) {
                        strBuf.append(list.get(j).getBillAccountName()).append("/");
                    }
                }
                String retStr = strBuf.toString().substring(0, (strBuf.length() - 1)).toString();

                FinAccountRelationDO result = new FinAccountRelationDO();
                result.setBillAccountName(retStr);
                result.setAccountNO(accountNO);
                resultList.add(result);
            }
        }
        return resultList;
    }
}
