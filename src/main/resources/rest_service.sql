CREATE DATABASE IF NOT EXISTS `rest_service`;

USE `rest_service`;

CREATE TABLE IF NOT EXISTS `projects` (
  `id` INT NOT NULL AUTO_INCREMENT,
  `name` VARCHAR(255) NULL DEFAULT NULL,
  PRIMARY KEY (`id`)
)
ENGINE = InnoDB
AUTO_INCREMENT = 4
DEFAULT CHARACTER SET = utf8mb3;

CREATE TABLE IF NOT EXISTS `employees` (
  `id` INT NOT NULL AUTO_INCREMENT,
  `name` VARCHAR(255) NULL DEFAULT NULL,
  `level` VARCHAR(20) NULL DEFAULT NULL,
  `type` VARCHAR(20) NULL DEFAULT NULL,
  `projects_id` INT NOT NULL,
  PRIMARY KEY (`id`),
  INDEX `fk_employees_projects_idx` (`projects_id` ASC) VISIBLE,
  CONSTRAINT `fk_employees_projects`
    FOREIGN KEY (`projects_id`)
    REFERENCES `projects` (`id`)
)
ENGINE = InnoDB
AUTO_INCREMENT = 7
DEFAULT CHARACTER SET = utf8mb3;

CREATE TABLE IF NOT EXISTS `employee_projects` (
  `projects_id` INT NOT NULL,
  `employees_id` INT NOT NULL,
  PRIMARY KEY (`projects_id`, `employees_id`),
  INDEX `fk_employee_projects_projects1_idx` (`projects_id` ASC) VISIBLE,
  INDEX `fk_employee_projects_employees1_idx` (`employees_id` ASC) VISIBLE,
  CONSTRAINT `fk_employee_projects_employees1`
    FOREIGN KEY (`employees_id`)
    REFERENCES `employees` (`id`),
  CONSTRAINT `fk_employee_projects_projects1`
    FOREIGN KEY (`projects_id`)
    REFERENCES `projects` (`id`)
)
ENGINE = InnoDB
DEFAULT CHARACTER SET = utf8mb3;

INSERT INTO `projects` (`id`, `name`)
VALUES
  (1, 'Project 1'),
  (2, 'Project 2'),
  (3, 'Project 3');

INSERT INTO `employees` (`id`, `name`, `level`, `type`, `projects_id`)
VALUES
  (1, 'John Doe', 'Junior', 'QA', 1),
  (2, 'Jane Smith', 'Mid', 'QA', 2),
  (3, 'David Johnson', 'Senior', 'QA', 3),
  (4, 'Michael Brown', 'Junior', 'DevOps', 1),
  (5, 'Jennifer Davis', 'Mid', 'DevOps', 2),
  (6, 'Christopher Wilson', 'Senior', 'DevOps', 3),
  (7, 'Jessica Miller', 'Junior', 'developer', 1),
  (8, 'Emily Taylor', 'Mid', 'developer', 2),
  (9, 'Daniel Martinez', 'Senior', 'developer', 3);
