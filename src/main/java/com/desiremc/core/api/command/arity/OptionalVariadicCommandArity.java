package com.desiremc.core.api.command.arity;

public class OptionalVariadicCommandArity implements CommandArity {

    @Override
    public boolean validateArity(int sentArgsLength, int commandArgsLength) {
        return sentArgsLength >= commandArgsLength - 1;
    }

}
