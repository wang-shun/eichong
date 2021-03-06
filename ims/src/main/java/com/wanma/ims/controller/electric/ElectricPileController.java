package com.wanma.ims.controller.electric;

import com.wanma.ims.common.domain.ElectricPileDO;
import com.wanma.ims.common.domain.UserDO;
import com.wanma.ims.common.domain.base.Pager;
import com.wanma.ims.common.dto.BaseResultDTO;
import com.wanma.ims.controller.BaseController;
import com.wanma.ims.controller.result.JsonException;
import com.wanma.ims.controller.result.JsonResult;
import com.wanma.ims.service.ElectricPileService;
import com.wanma.ims.util.ErrorHtmlUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.util.List;

/**
 * 电桩管理-电桩管理
 * xyc
 */
@RequestMapping("/manage/electric")
@Controller
public class ElectricPileController extends BaseController {
    private final Logger LOGGER = LoggerFactory.getLogger(this.getClass());

    @Autowired
    private ElectricPileService electricPileService;

    /**
     * 查询电桩列表
     */
    @RequestMapping(value = "/getElectricPileList", method = RequestMethod.POST)
    @ResponseBody
    public void getElectricPileList(ElectricPileDO searchModel, Pager pager) {
        JsonResult result = new JsonResult();
        UserDO loginUser = getCurrentLoginUser();

        try {
            long total = electricPileService.countElectricPile(searchModel, loginUser);
            if (total <= pager.getOffset()) {
                pager.setPageNo(1L);
            }
            pager.setTotal(total);
            searchModel.setPager(pager);
            List<ElectricPileDO> electricPileList = electricPileService.getElectricPileList(searchModel, loginUser);
            result.setPager(pager);
            result.setDataObject(electricPileList);
            responseJson(result);
        } catch (Exception e) {
            LOGGER.error("ElectricPileController-getElectricPileList is error|searchModel={}|loginUser={}", searchModel, loginUser, e);
            responseJson(new JsonException("查询电桩列表失败！"));
        }
    }

    /**
     * 查询所有电桩
     */
    @RequestMapping(value = "/getAllElectricPileList", method = RequestMethod.POST)
    @ResponseBody
    public void getAllElectricPileList(ElectricPileDO searchModel) {
        JsonResult result = new JsonResult();
        UserDO loginUser = getCurrentLoginUser();

        try {
            List<ElectricPileDO> electricPileList = electricPileService.getElectricPileList(searchModel, loginUser);
            result.setDataObject(electricPileList);

            responseJson(result);
        } catch (Exception e) {
            LOGGER.error("ElectricPileController-getAllElectricPileList is error|searchModel={}|loginUser={}", searchModel, loginUser, e);
            responseJson(new JsonException("查询所有电桩失败！"));
        }
    }

    /***
     * 检查电桩编号的唯一性
     */
    @RequestMapping(value = "/checkCode", method = RequestMethod.POST)
    @ResponseBody
    public void checkElectricPileCodeIsUnique(String code) {
        JsonResult result = new JsonResult();
        UserDO loginUser = getCurrentLoginUser();

        try {
            boolean isUnique = electricPileService.checkElectricPileCodeIsUnique(code);
            result.setDataObject(isUnique);

            responseJson(result);
        } catch (Exception e) {
            LOGGER.error("ElectricPileController-checkElectricPileCodeIsUnique is error|code={}|loginUser={}", code, loginUser, e);
            responseJson(new JsonException("检查电桩编号的唯一性失败！"));
        }
    }

    /***
     * 添加电桩
     */
    @RequestMapping(value = "/add", method = RequestMethod.POST)
    @ResponseBody
    public void addElectricPile(ElectricPileDO electricPile) {
        JsonResult result = new JsonResult();
        UserDO loginUser = getCurrentLoginUser();

        try {
            BaseResultDTO resultDTO = electricPileService.addElectricPile(electricPile, loginUser);
            if (resultDTO.isFailed()) {
                result = new JsonResult(false, resultDTO.getResultCode(), resultDTO.getErrorDetail());
            }
            responseJson(result);
        } catch (Exception e) {
            LOGGER.error("ElectricPileController-addElectricPile is error|newElectricPile={}|loginUser={}", electricPile, loginUser, e);
            responseJson(new JsonException("添加电桩失败！"));
        }
    }

    /***
     * 删除电桩
     */
    @RequestMapping(value = "/del", method = RequestMethod.POST)
    @ResponseBody
    public void deleteElectricPile(String ids) {
        JsonResult result = new JsonResult();
        UserDO loginUser = getCurrentLoginUser();

        try {
            BaseResultDTO resultDTO = electricPileService.deleteElectricPile(ids, loginUser);
            if (resultDTO.isFailed()) {
                result = new JsonResult(false, resultDTO.getResultCode(), resultDTO.getErrorDetail());
            }

            responseJson(result);
        } catch (Exception e) {
            LOGGER.error("ElectricPileController-deleteElectricPile is error|delIds={}|loginUser={}", ids, loginUser, e);
            responseJson(new JsonException("删除电桩失败！"));
        }
    }

    /***
     * 修改电桩
     */
    @RequestMapping(value = "/modify", method = RequestMethod.POST)
    @ResponseBody
    public void modifyElectricPile(ElectricPileDO electricPile) {
        JsonResult result = new JsonResult();
        UserDO loginUser = getCurrentLoginUser();

        try {
            BaseResultDTO resultDTO = electricPileService.modifyElectricPile(electricPile, loginUser);
            if (resultDTO.isFailed()) {
                result = new JsonResult(false, resultDTO.getResultCode(), resultDTO.getErrorDetail());
            }

            responseJson(result);
        } catch (Exception e) {
            LOGGER.error("ElectricPileController-modifyElectricPile is error|modifyElectricPile={}|loginUser={}", electricPile, loginUser, e);
            responseJson(new JsonException("修改电桩失败！"));
        }
    }

    /***
     * 查看电桩
     */
    @RequestMapping(value = "/get", method = RequestMethod.POST)
    @ResponseBody
    public void getElectricPile(Long electricPileId) {
        UserDO loginUser = getCurrentLoginUser();

        try {
            ElectricPileDO electricPile = electricPileService.getElectricPileById(electricPileId, loginUser);
            if (electricPile != null) {
                responseJson(electricPile);
            } else {
                responseJson(new JsonException("查看电桩失败，请刷新页面后再试！"));
            }
        } catch (Exception e) {
            LOGGER.error("ElectricPileController-getElectricPile is error|electricPileId={}|loginUser={}", electricPileId, loginUser, e);
            responseJson(new JsonException("查看电桩失败！"));
        }
    }

    /**
     * 导入电桩
     */
    @RequestMapping(value = "/import", method = RequestMethod.POST)
    @ResponseBody
    public void importElectricPile(@RequestParam(value = "file", required = false) MultipartFile multipartFile) {
        JsonResult result = new JsonResult();
        UserDO loginUser = getCurrentLoginUser();

        try {
            BaseResultDTO resultDTO = electricPileService.importElectricPile(multipartFile, loginUser);
            if (resultDTO.isFailed()) {
                result = new JsonResult(false, resultDTO.getResultCode(), resultDTO.getErrorDetail());
            }

            responseJson(result);
        } catch (Exception e) {
            LOGGER.error("ElectricPileController-importElectricPile is error|multipartFileName={}|loginUser={}", multipartFile.getOriginalFilename(), loginUser, e);
            responseJson(new JsonException(e.getMessage()));
        }
    }

    /**
     * 导出电桩
     */
    @RequestMapping(value = "/export", method = RequestMethod.GET)
    @ResponseBody
    public void exportElectricPile(HttpServletResponse response, ElectricPileDO searchModel) {
        UserDO loginUser = getCurrentLoginUser();

        try {
            electricPileService.exportElectricPile(response, searchModel, loginUser);
        } catch (Exception e) {
            LOGGER.error("ElectricPileController-exportElectricPile is error|searchModel={}|loginUser={}", searchModel, loginUser, e);
            ErrorHtmlUtil.createErrorPage(response, "导出电桩信息失败！");
        }
    }

    /***
     * 解绑电桩
     */
    @RequestMapping(value = "/unbind", method = RequestMethod.POST)
    @ResponseBody
    public void unbindElectricPile(String electricPileIds, Integer bindType) {
        JsonResult result = new JsonResult();
        UserDO loginUser = getCurrentLoginUser();

        try {
            BaseResultDTO resultDTO = electricPileService.unbindElectricPile(electricPileIds, bindType, loginUser);
            if (resultDTO.isFailed()) {
                result = new JsonResult(false, resultDTO.getResultCode(), resultDTO.getErrorDetail());
            }

            responseJson(result);
        } catch (Exception e) {
            LOGGER.error("ElectricPileController-unbindElectricPile is error|electricPileIds={}|bindType={}|loginUser={}", electricPileIds, bindType, loginUser, e);
            responseJson(new JsonException("解绑电桩失败！"));
        }
    }

    /***
     * 修改资产归属
     */
    @RequestMapping(value = "/modifyElectricPileCompany", method = RequestMethod.POST)
    @ResponseBody
    public void modifyElectricPileCompany(String electricPileIds, Long companyId, Long companyNumber) {
        JsonResult result = new JsonResult();
        UserDO loginUser = getCurrentLoginUser();

        try {
            BaseResultDTO resultDTO = electricPileService.modifyElectricPileCompany(electricPileIds, companyId, companyNumber, loginUser);
            if (resultDTO.isFailed()) {
                result = new JsonResult(false, resultDTO.getResultCode(), resultDTO.getErrorDetail());
            }

            responseJson(result);
        } catch (Exception e) {
            LOGGER.error("ElectricPileController-modifyElectricPileCompany is error|electricPileIds={}|companyId={}|companyNumber={}|loginUser={}", electricPileIds, companyId, companyNumber, loginUser, e);
            responseJson(new JsonException("修改资产归属失败！"));
        }
    }

    /***
     * 审核上线电桩
     */
    @RequestMapping(value = "/auditElectricPile", method = RequestMethod.POST)
    @ResponseBody
    public void auditElectricPile(String electricPileIds) {
        JsonResult result = new JsonResult();
        UserDO loginUser = getCurrentLoginUser();

        try {
            BaseResultDTO resultDTO = electricPileService.auditElectricPile(electricPileIds, loginUser);
            if (resultDTO.isFailed()) {
                result = new JsonResult(false, resultDTO.getResultCode(), resultDTO.getErrorDetail());
            }

            responseJson(result);
        } catch (Exception e) {
            LOGGER.error("ElectricPileController-auditElectricPile is error|electricPileIds={}|loginUser={}", electricPileIds, loginUser, e);
            responseJson(new JsonException("审核上线电桩失败！"));
        }
    }
}