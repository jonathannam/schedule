package com.agribank.schedule.utils;

public enum RoleEnum {
	ADMIN(1, "ROLE_ADMIN"), SHOP(2, "ROLE_SHOP"), STAFF(3, "ROLE_STAFF"), MEMBER(4, "ROLE_MEMBER");

	private int roleId;
	private String roleName;

	RoleEnum(int roleId, String roleName) {
		this.roleId = roleId;
		this.roleName = roleName;
	}

	public int getRoleId() {
		return roleId;
	}

	public void setRoleId(int roleId) {
		this.roleId = roleId;
	}

	public String getRoleName() {
		return roleName;
	}

	public void setRoleName(String roleName) {
		this.roleName = roleName;
	}
}
