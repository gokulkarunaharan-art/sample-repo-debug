package com.gokul.librarymanagement.model;

import lombok.Getter;

import java.util.Set;
@Getter
public enum Role {
    LIBRARIAN(Set.of(Permission.ADD,Permission.REMOVE,Permission.BORROW,Permission.RETURN)), STAFF(Set.of(Permission.BORROW,Permission.RETURN));

    Set<Permission> permissions;

    Role(Set<Permission> permissions){
        this.permissions = permissions;
    }
}
