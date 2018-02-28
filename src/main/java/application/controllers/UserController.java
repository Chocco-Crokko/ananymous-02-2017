package application.controllers;

import application.models.User;
import application.services.AccountService;
import application.utils.Validator;
import application.utils.requests.PasswordRequest;
import application.utils.requests.ScoreRequest;
import application.utils.responses.FullUserResponse;
import application.utils.responses.MessageResponse;
import org.jetbrains.annotations.NotNull;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpSession;

@RestController
@CrossOrigin/*(origins = {"https:/soul-hunting.ru", "localhost"})*/
@RequestMapping("/api")
@Transactional
public class UserController extends BaseController {
    private static final int USERS_ON_PAGE = 5;

    public UserController(@NotNull AccountService accountService)
    {
        super(accountService);
    }

    @GetMapping(path = "/users/{id}", produces = "application/json")
    public ResponseEntity getUser(@PathVariable Long id)
    {
        final User user = accountService.getUser(id);
        if (user == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(new MessageResponse(String.format("id: %s, user not found", id)));
        }
        return ResponseEntity.ok(new FullUserResponse(user));
    }

    @GetMapping(path = "/users", produces = "application/json")
    public ResponseEntity getBestUsers(@RequestParam(value = "page", defaultValue = "0") int page)
    {
        if (page < 0) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new MessageResponse("Invalid page number"));
        } else if (page == 0) {
            return ResponseEntity.ok(accountService.getBestUsers());
        } else {
            return ResponseEntity.ok(accountService.getBestUsers((page - 1) * USERS_ON_PAGE, USERS_ON_PAGE));
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

    @PostMapping(path = "/score", consumes = "application/json", produces = "application/json")
    public ResponseEntity addScore(@RequestBody ScoreRequest body) {
        Long id = body.getId();
        if (id == null) {
            final String username = body.getUsername();
            if (username == null) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                        .body(new MessageResponse("No id and username"));
            } else {
                id = accountService.getUserID(username);
                if (id == null) {
                    return ResponseEntity.status(HttpStatus.NOT_FOUND)
                            .body(new MessageResponse(String.format("user %s not found", username)));
                }
            }
        }
        final boolean success = accountService.addScore(id, body.getsScore(), body.getmScore());
        if (!success) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(new MessageResponse(String.format("Coundn't add score for id %d", id)));
        }
        return ResponseEntity.ok(new MessageResponse("Success"));
    }
}
