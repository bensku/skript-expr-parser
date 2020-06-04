package io.github.syst3ms.skriptparser.lang;

import io.github.syst3ms.skriptparser.event.TriggerContext;
import io.github.syst3ms.skriptparser.file.FileSection;
import io.github.syst3ms.skriptparser.log.SkriptLogger;
import io.github.syst3ms.skriptparser.parsing.ParserState;
import io.github.syst3ms.skriptparser.parsing.ScriptLoader;

import java.util.Collections;
import java.util.List;

/**
 * The entry point for all code in Skript. Once an event triggers, all of the code inside it is run
 */
public abstract class SkriptEvent implements SyntaxElement {
    /**
     * Whether this event should trigger, given the {@link TriggerContext}
     * @param ctx the TriggerContext to check
     * @return whether the event should trigger
     */
    public abstract boolean check(TriggerContext ctx);

    List<Statement> loadSection(FileSection section, ParserState parserState, SkriptLogger logger) {
        return ScriptLoader.loadItems(section, parserState, logger);
    }

    /**
     * A list of the classes of every syntax that is allowed to be used inside of this CodeSection. The default behavior
     * is to return an empty list, which equates to no restrictions. If overriden, this allows the creation of specialized,
     * DSL-like sections in which only select {@linkplain Statement statements} and other {@linkplain CodeSection sections}
     * (and potentially, but not necessarily, expressions).
     * @return a list of the classes of each syntax allowed inside this CodeSection
     * @see #isRestrictingExpressions()
     */
    protected List<Class<? extends SyntaxElement>> getAllowedSyntaxes() {
        return Collections.emptyList();
    }

    /**
     * Whether the syntax restrictions outlined in {@link #getAllowedSyntaxes()} should also apply to expressions.
     * This is usually undesirable, so it is false by default.
     *
     * This should return true <b>if and only if</b> {@link #getAllowedSyntaxes()} contains an {@linkplain Expression} class.
     * @return whether the use of expressions is also restricted by {@link #getAllowedSyntaxes()}. False by default.
     */
    protected boolean isRestrictingExpressions() {
        return false;
    }
}
