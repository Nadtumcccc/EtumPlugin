package be.nadtum.etum.Utility.Modules;

import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

public class PrefixMessage {

    @Contract(pure = true)
    public static @NotNull String admin(){
        return "§b§lADMIN §8|§e ";
    }

    @Contract(pure = true)
    public static @NotNull String erreur(){
        return "§e§lINFO §c| ";
    }

    @Contract(pure = true)
    public static @NotNull String chat(){
        return "§e[§bChat§e]§e ";
    }

    @Contract(pure = true)
    public static @NotNull String serveur(){
        return "§e§lINFO §8|§a ";
    }
    @Contract(pure = true)
    public static @NotNull String logs(){
        return "§fLOGS §7|§f ";
    }

}
