package ru.itis.maletskov.internship.util.validator;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;
import ru.itis.maletskov.internship.form.UserForm;
import ru.itis.maletskov.internship.repository.UserRepository;

@Component
@RequiredArgsConstructor
public class UserFormValidator implements Validator {
    private final UserRepository userRepository;

    @Override
    public boolean supports(Class<?> clazz) {
        return UserForm.class.equals(clazz);
    }

    @Override
    public void validate(Object userForm, Errors errors) {
        UserForm form = (UserForm) userForm;
        if (checkLoginExists(form.getLogin())) {
            errors.rejectValue(FieldNames.LOGIN.getName(), "user.form.login.error");
        }
        if (form.getPassword().length() < ValidatorConstraints.PASSWORD_MIN_LENGTH ||
                form.getPassword().length() > ValidatorConstraints.PASSWORD_MAX_LENGTH) {
            errors.rejectValue(FieldNames.PASSWORD.getName(), "user.form.password.length.error");
        }
    }

    private boolean checkLoginExists(String login) {
        return userRepository.existsUserByLogin(login);
    }
}
