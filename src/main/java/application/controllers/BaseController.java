package application.controllers;

import application.services.AccountService;
import org.jetbrains.annotations.NotNull;


public class BaseController {
    @NotNull
    public static final String USER_ID = "userId";
    @NotNull
    protected final AccountService accountService;

    public BaseController(@NotNull AccountService accountService) {
        this.accountService = accountService;
    }
}
