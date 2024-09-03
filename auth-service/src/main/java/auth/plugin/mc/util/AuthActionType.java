package auth.plugin.mc.util;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Getter
public enum AuthActionType {

    LOGIN("login"), REGISTER("register");

    private final String value;

}
