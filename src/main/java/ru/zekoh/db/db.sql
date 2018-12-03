CREATE TABLE `check` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `amount_by_price` decimal(10,2) NOT NULL DEFAULT '0.00',
  `total` decimal(10,2) NOT NULL DEFAULT '0.00',
  `payment_state` tinyint(1) NOT NULL DEFAULT '0',
  `discount_on_goods` tinyint(1) DEFAULT '0',
  `discount_on_check` tinyint(1) DEFAULT '0',
  `type_of_payment` int(1) DEFAULT '1',
  `date_of_creation` datetime DEFAULT CURRENT_TIMESTAMP,
  `date_of_closing` datetime DEFAULT NULL,
  `return_status` tinyint(1) DEFAULT '0',
  `is_a_live` tinyint(1) DEFAULT '1',
  `contain_goods` tinyint(1) DEFAULT '0',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=168 DEFAULT CHARSET=latin1;

CREATE TABLE `goods` (
  `check_id` int(11) DEFAULT NULL,
  `general_id` int(11) DEFAULT NULL,
  `product_id` int(11) DEFAULT NULL,
  `product_name` varchar(255) CHARACTER SET utf8 DEFAULT NULL,
  `quantity` decimal(10,3) DEFAULT NULL,
  `price_from_the_price_list` decimal(10,2) NOT NULL DEFAULT '0.00',
  `price_after_discount` decimal(10,2) DEFAULT '0.00',
  `selling_price` decimal(10,2) DEFAULT '0.00'
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

CREATE TABLE `product` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `name` varchar(255) CHARACTER SET utf8 DEFAULT NULL,
  `price` decimal(10,2) DEFAULT '0.00',
  `folder` tinyint(1) DEFAULT '1',
  `parent_id` int(11) DEFAULT '1',
  `general_id` int(11) DEFAULT '1',
  `is_a_live` tinyint(1) DEFAULT '1',
  `classifier` int(11) DEFAULT '0',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=5 DEFAULT CHARSET=latin1;

CREATE TABLE `role` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `name` varchar(255) CHARACTER SET utf8 NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=5 DEFAULT CHARSET=latin1;

CREATE TABLE `session` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `user_id` int(11) NOT NULL,
  `name` varchar(255) CHARACTER SET utf8 DEFAULT NULL,
  `role_id` int(11) NOT NULL,
  `role` varchar(255) CHARACTER SET utf8 NOT NULL,
  `date_and_time_of_session_creation` datetime DEFAULT CURRENT_TIMESTAMP,
  `date_and_time_of_session_closing` datetime DEFAULT NULL,
  `is_a_live` tinyint(1) NOT NULL DEFAULT '1',
  PRIMARY KEY (`id`),
  UNIQUE KEY `session_id_uindex` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=145 DEFAULT CHARSET=latin1;

CREATE TABLE `user` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `name` varchar(255) CHARACTER SET utf8 NOT NULL,
  `login` varchar(255) NOT NULL,
  `password` varchar(255) NOT NULL,
  `mail` varchar(255) DEFAULT NULL,
  `role_id` int(11) NOT NULL DEFAULT '1',
  `is_a_live` tinyint(1) DEFAULT '1',
  PRIMARY KEY (`id`),
  UNIQUE KEY `user_id_uindex` (`id`),
  UNIQUE KEY `user_login_uindex` (`login`)
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=latin1;