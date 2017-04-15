package application.controllers;

import application.models.User;
import application.services.AccountService;
import application.utils.Validator;
import application.utils.requests.UserRequest;
import application.utils.requests.UsernameRequest;
import application.utils.responses.FullUserResponse;
import application.utils.responses.IdResponse;
import application.utils.responses.MessageResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpSession;
import javax.validation.constraints.NotNull;

@RestController
@CrossOrigin/*(origins = {"https:/soul-hunting.ru", "localhost"})*/
@RequestMapping("/api")
public class SessionController extends BaseController {

    public SessionController(@NotNull AccountService accountService)
    {
        super(accountService);
    }

    @PostMapping(path = "/signup", consumes = "application/json", produces = "application/json")
    public ResponseEntity signup(@RequestBody UserRequest body, HttpSession httpSession)
    {
        final String error = Validator.getUserError(body);

        if (error != null) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(new MessageResponse(error));
        } else if (httpSession.getAttribute(USER_ID) != null) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN)
                    .body(new MessageResponse("User logged in this session"));
        }

        final Long id = accountService.signup(body);
        if (id == null) {
            return ResponseEntity.status(HttpStatus.CONFLICT)
                    .body(new MessageResponse(String.format("User %s already exist", body.getLogin())));
        }

        httpSession.setAttribute(USER_ID, id);
        return ResponseEntity.ok(new IdResponse(id));
    }

    @PostMapping(path = "/signin", consumes = "application/json", produces = "application/json")
    public ResponseEntity signin(@RequestBody UsernameRequest body, HttpSession httpSession)
    {
        final String error = Validator.getUserRequestError(body);

        if (error != null) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(new MessageResponse(error));
        } else if (httpSession.getAttribute(USER_ID) != null) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN)
                    .body(new MessageResponse("User logged in this session"));
        }

        final String username = body.getUsername();
        final Long id = accountService.getUserID(username);

        if (id == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(new MessageResponse(String.format("user %s not found", username)));
        } else if (!accountService.checkUserAccount(id, body.getPassword())) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN)
                    .body(new MessageResponse(String.format("Wrong password for user %s", username)));
        }

        httpSession.setAttribute(USER_ID, id);
        return ResponseEntity.ok(new IdResponse(id));
    }

    @PostMapping(path = "/logout", produces = "application/json")
    public ResponseEntity logout(HttpSession httpSession)
    {
        if (httpSession.getAttribute(USER_ID) == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(new MessageResponse("User not logged in"));
        }
        httpSession.removeAttribute(USER_ID);
        return ResponseEntity.ok(new MessageResponse("Success"));
    }

    @GetMapping(path = "/cur-user", produces = "application/json")
    public ResponseEntity getCurrentUser(HttpSession httpSession) {
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
        return ResponseEntity.ok(new FullUserResponse(id, user.getLogin(), user.getEmail()));
    }
}
