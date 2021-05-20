DROP TABLE `global_library`.`extradition`;
ALTER TABLE `global_library`.`request`
    RENAME COLUMN `date` TO `date_creation`;
ALTER TABLE `global_library`.`request`
    ADD COLUMN `date_extradition` TIMESTAMP NULL AFTER `date_creation`;
ALTER TABLE `global_library`.`request`
    ADD COLUMN `date_return` TIMESTAMP NULL AFTER `date_extradition`;