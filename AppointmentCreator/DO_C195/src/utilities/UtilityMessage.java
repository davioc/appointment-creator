package utilities;

/**
 * This interface is used for reducing the need to write extra String values in some methods of controller classes.
 */
@FunctionalInterface
public interface UtilityMessage {

    String genMsg(String s);

}
