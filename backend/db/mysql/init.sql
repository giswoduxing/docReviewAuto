CREATE TABLE IF NOT EXISTS sys_user (
  id BIGINT NOT NULL,
  username VARCHAR(64) NOT NULL,
  display_name VARCHAR(128) NOT NULL,
  email VARCHAR(128) NOT NULL,
  status VARCHAR(32) NOT NULL,
  created_at DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
  updated_at DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  is_deleted TINYINT(1) NOT NULL DEFAULT 0,
  PRIMARY KEY (id),
  UNIQUE KEY uk_sys_user_username (username),
  KEY idx_sys_user_status (status),
  KEY idx_sys_user_deleted (is_deleted)
) ENGINE = InnoDB DEFAULT CHARSET = utf8mb4;

INSERT INTO sys_user (id, username, display_name, email, status)
VALUES
  (1, 'framework-admin', 'Framework Admin', 'framework-admin@example.com', 'ACTIVE')
ON DUPLICATE KEY UPDATE
  display_name = VALUES(display_name),
  email = VALUES(email),
  status = VALUES(status);
