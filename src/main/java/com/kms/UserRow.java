package com.kms;

import com.annotations.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserRow {

  @Column("ID")
  @NotNull
  private int ID;

  @Column("Email")
  @NotNull
  private String email;

  @Column("First Name")
  private String firstName;

  @Column("Last Name")
  private String lastName;
}
