package io.github.syst3ms.skriptparser.lang;

/**
 * A context under which a trigger may be run.
 * A parallel to Bukkit's own Event class.
 */
public interface TriggerContext {
    /**
     * A dummy TriggerContext that may be used when the context is known not to matter.
     */
    TriggerContext DUMMY = () -> "dummy";

    String getName();
}
