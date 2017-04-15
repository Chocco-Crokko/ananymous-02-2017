package application.controllers;

import application.models.User;
import application.services.AccountService;
import application.utils.Validator;
import application.utils.requests.PasswordRequest;
import application.utils.responses.FullUserResponse;
import application.utils.responses.MessageResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpSession;
import javax.validation.constraints.NotNull;

@RestController
@CrossOrigin/*(origins = {"https:/soul-hunting.ru", "localhost"})*/
@RequestMapping("/api")
public class UserController extends BaseController {
    private static final int USERS_ON_PAGE = 5;

    public UserController(@NotNull AccountService accountService)
    {
        super(accountService);
    }

    // TODO remove this
    @GetMapping(path = "/do-not-call")
    public ResponseEntity clear() {
        accountService.clear();
        return ResponseEntity.ok("OK");
    }

    @GetMapping(path = "/users/{id}", produces = "application/json")
    public ResponseEntity getUser(@PathVariable Long id)
    {
        final User user = accountService.getUser(id);
        if (user == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(new MessageResponse(String.format("id: %s, user not found", id)));
        }
        return ResponseEntity.ok(new FullUserResponse(user.getId(), user.getLogin(), user.getEmail()));
    }

    @GetMapping(path = "/users", produces = "application/json")
    public ResponseEntity getUsers(@RequestParam(value = "page", defaultValue = "0") int page)
    {
        if (page < 0) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new MessageResponse("Invalid page number"));
        } else if (page == 0) {
            return ResponseEntity.ok(accountService.getUsers());
        } else {
            return ResponseEntity.ok(accountService.getUsers((page - 1) * USERS_ON_PAGE, USERS_ON_PAGE));
        }
    }

    @PostMapping(path = "/change-pass", consumes = "application/json", produces = "application/json")
    public ResponseEntity changePassword(@RequestBody PasswordRequest body, HttpSession httpSession)
    {
        final String oldPasswordError;
        final String newPasswordError;
        if ((oldPasswordError = Validator.checkPassword(body.getOldPassword())) != null) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(new MessageResponse(oldPasswordError));
        } else if ((newPasswordError = Validator.checkPassword(body.getNewPassword())) != null) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(new MessageResponse(newPasswordError));
        }
        final Long id = (Long) httpSession.getAttribute(USER_ID);
        if (id == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(new MessageResponse("User not logged in"));
        }
        final User user = accountService.getUser(id);
        if (user == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(new MessageResponse(String.format("id: %s, bad cookies", id)));
        }
        final boolean isSuccess = accountService.changePassword(user, body.getOldPassword(), body.getNewPassword());
        if (!isSuccess) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN)
                    .body(new MessageResponse(String.format("id: %s, wrong password", id)));
        }
        return ResponseEntity.ok(new MessageResponse("Success"));
    }
}
