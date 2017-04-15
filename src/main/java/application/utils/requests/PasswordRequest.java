package application.utils.requests;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

import javax.validation.constraints.NotNull;

public class PasswordRequest {
  @NotNull
  private final String oldPassword;
  @NotNull
  private final String newPassword;

  @JsonCreator
  public PasswordRequest(@JsonProperty("oldPassword") @NotNull String oldPassword,
                         @JsonProperty("newPassword") @NotNull String newPassword)
  {
    this.oldPassword = oldPassword;
    this.newPassword = newPassword;
  }

  @NotNull
  public String getOldPassword()
  {
    return oldPassword;
  }

  @NotNull
  public String getNewPassword()
  {
    return newPassword;
  }
}