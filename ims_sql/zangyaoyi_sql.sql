ALTER TABLE `tbl_chargingrecord` ADD COLUMN  `chRe_JMoney` decimal(8,4) DEFAULT NULL COMMENT '尖时段服务费' AFTER `chre_usrgateip`; 
 ALTER TABLE  `tbl_chargingrecord` ADD COLUMN    `chRe_FMoney` decimal(8,4) DEFAULT NULL COMMENT '峰时段服务费' AFTER `chRe_JMoney`; 
 ALTER TABLE  `tbl_chargingrecord` ADD COLUMN   `chRe_PMoney` decimal(8,4) DEFAULT NULL COMMENT '平时段服务费' AFTER `chRe_FMoney`; 
 ALTER TABLE  `tbl_chargingrecord` ADD COLUMN   `chRe_GMoney` decimal(8,4) DEFAULT NULL COMMENT '谷时段服务费' AFTER `chRe_PMoney`; 

 #ALTER TABLE  `tbl_rateinformation` CHANGE `raIn_type` `raIn_type` SMALLINT(1) DEFAULT 1 NOT NULL COMMENT '1:费率2位，2：费率4位,3:个性化费率';
 #ALTER TABLE  `tbl_rateinformation` ADD COLUMN  `raIn_TipTimeTariffMoney` decimal(8,4) DEFAULT NULL COMMENT '尖时段服务费' AFTER `raIn_type`; 
 #ALTER TABLE  `tbl_rateinformation` ADD COLUMN    `raIn_PeakElectricityMoney` decimal(8,4) DEFAULT NULL COMMENT '峰时段服务费' AFTER `raIn_TipTimeTariffMoney`; 
 #ALTER TABLE  `tbl_rateinformation` ADD COLUMN   `raIn_UsualMoney` decimal(8,4) DEFAULT NULL COMMENT '平时段服务费' AFTER `raIn_PeakElectricityMoney`; 
 #ALTER TABLE  `tbl_rateinformation` ADD COLUMN   `raIn_ValleyTimeMoney` decimal(8,4) DEFAULT NULL COMMENT '谷时段服务费' AFTER `raIn_UsualMoney`; 
 
 
 ALTER TABLE `tbl_rateinformation` ADD COLUMN `raIn_TipTimeTariffMoney` decimal(8,4) DEFAULT NULL COMMENT '尖时段服务费'; 
 ALTER TABLE `tbl_rateinformation` ADD COLUMN `raIn_Name` varchar(32) NOT NULL DEFAULT '' COMMENT '费率名称';
 ALTER TABLE `tbl_rateinformation` ADD COLUMN `raIn_PeakElectricityMoney` decimal(8,4) DEFAULT NULL COMMENT '峰时间段服务费'; 
 ALTER TABLE `tbl_rateinformation` ADD COLUMN `raIn_UsualMoney` decimal(8,4) DEFAULT NULL COMMENT '平时段服务费';
 ALTER TABLE `tbl_rateinformation` ADD COLUMN `raIn_ValleyTimeMoney` decimal(8,4) DEFAULT NULL COMMENT '谷时段服务费';
 ALTER TABLE `tbl_rateinformation` CHANGE COLUMN `raIn_TipTimeTariff` `raIn_TipTimeTariffPrice`  decimal(6,4) NULL DEFAULT NULL COMMENT '尖时段电价' AFTER `raIn_TimeMarker`;
 ALTER TABLE `tbl_rateinformation` CHANGE COLUMN `userId` `creator_id` varchar(50) NOT NULL DEFAULT '' COMMENT '添加费率的用户，p_m_user表的id';
 ALTER TABLE  `tbl_rateinformation` CHANGE `raIn_type` `raIn_type` SMALLINT(1) DEFAULT 1 NOT NULL COMMENT '1:费率2位，2：费率4位,3:个性化费率';
ALTER TABLE `tbl_company` ADD COLUMN `third_flag` INT(4) DEFAULT 0 NOT NULL COMMENT '对接关系0:无对接，1:公共业务，2:充电业务' AFTER `cpy_num`; 
 update tbl_rateinformation
 set raIn_TipTimeTariffMoney = raIn_ServiceCharge,
 raIn_PeakElectricityMoney = raIn_ServiceCharge,
 raIn_UsualMoney = raIn_ServiceCharge,
 raIn_ValleyTimeMoney = raIn_ServiceCharge;

ALTER TABLE `tbl_chargingorder` ADD COLUMN `bill_account_id` BIGINT(11) DEFAULT 0  NOT NULL COMMENT '账单科目ID' ;
UPDATE `tbl_chargingorder` SET `bill_account_id` = '1' ;
ALTER TABLE  `tbl_chargingorder` ADD COLUMN `cv_vin_code` VARCHAR(20) DEFAULT '' NOT NULL COMMENT 'VIN码' AFTER `bill_account_id`; 

ALTER TABLE  `tbl_purchasehistory` ADD COLUMN `account_id` BIGINT(1) DEFAULT 0 NOT NULL COMMENT '资金账户id' ;

ALTER TABLE `tbl_chargingrecord` ADD COLUMN `account_id` BIGINT(11) DEFAULT 0  NOT NULL COMMENT '资金账户ID' AFTER `chRe_GMoney`; 

ALTER TABLE `tbl_chargingrecord` ADD INDEX index_user_id ( `user_id` ); 


#以下是迁移脚本

DELIMITER ;;

#1
CREATE  PROCEDURE `proc_charging_record_accountId`()
BEGIN
        DECLARE userId        BIGINT(11);                                #用户Id
        DECLARE accountId        BIGINT(11);                        #资金账户Id
        DECLARE num                 BIGINT DEFAULT 0;                    #数量
        DECLARE i INT;                                                                #偏移量

        #充电记录游标
        DECLARE chargingrecord_cursor CURSOR FOR
            SELECT user_id FROM tbl_chargingrecord GROUP BY user_id;

        SET i = 0;
        SELECT COUNT(*) INTO num FROM (SELECT user_id FROM tbl_chargingrecord GROUP BY user_id) s;
        OPEN chargingrecord_cursor;
            WHILE i < num DO
                FETCH chargingrecord_cursor INTO userId ;

                SELECT account_id INTO accountId FROM fin_account_relation 
                        WHERE user_id = userId AND bill_account_id = 1 AND priority = 1;

                IF(accountId IS NOT NULL) THEN
                    UPDATE tbl_chargingrecord SET account_id = accountId WHERE user_id = userId;
                END IF;

                SET i = i + 1;
            END WHILE;
        CLOSE chargingrecord_cursor;
END;;

DELIMITER ;


CALL `proc_charging_record_accountId` ;

INSERT INTO `order_fav_record`
(order_code,cpy_id,user_id,bill_account_id,favourable_id,account_id,favourable_money,creator,MODIFIER,gmt_creator,gmt_modified)
SELECT s.puHi_TransactionNumber,chr.chRe_OrgNo, s.puHi_UserId,1,1,chr.account_id,ROUND(s.puHi_Monetary*100), "admin","", s.puHi_Createdate, s.puHi_Updatedate
FROM (SELECT se.puHi_TransactionNumber, se.puHi_UserId, ROUND(se.puHi_Monetary*100) puHi_Monetary, se.puHi_Createdate, se.puHi_Updatedate
 FROM tbl_purchasehistory se WHERE se.puHi_Type = 6) s INNER JOIN tbl_chargingrecord chr ON s.puHi_TransactionNumber= chr.chRe_TransactionNumber;

UPDATE order_fav_record ofr,tbl_company tc SET ofr.cpy_id =tc.cpy_id  WHERE ofr.cpy_id=tc.cpy_number; 