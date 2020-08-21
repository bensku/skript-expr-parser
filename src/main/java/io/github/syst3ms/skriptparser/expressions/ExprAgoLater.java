package io.github.syst3ms.skriptparser.expressions;

import io.github.syst3ms.skriptparser.Main;
import io.github.syst3ms.skriptparser.lang.Expression;
import io.github.syst3ms.skriptparser.lang.TriggerContext;
import io.github.syst3ms.skriptparser.parsing.ParseContext;
import io.github.syst3ms.skriptparser.util.SkriptDate;
import org.jetbrains.annotations.Nullable;

import java.time.Duration;

/**
 * The date that was a certain duration ago or is a certain duration in the future.
 *
 * @name Ago/Later
 * @type EXPRESSION
 * @pattern %duration% (ago|in the past|before [the] [date] %date%)
 * @pattern %duration% (later|in the future|(from|after) [the] [date] %date%)
 * @since ALPHA
 * @author Mwexim
 */
public class ExprAgoLater implements Expression<SkriptDate> {

	static {
		Main.getMainRegistration().addExpression(
				ExprAgoLater.class,
				SkriptDate.class,
				true,
				"%duration% (ago|in the past|1:before [the] [date] %date%)",
						"%duration% (later|in the future|1:(from|after) [the] [date] %date%)");
	}

	Expression<Duration> duration;
	Expression<SkriptDate> date;
	boolean past;
	boolean relative;

	@SuppressWarnings("unchecked")
	@Override
	public boolean init(Expression<?>[] expressions, int matchedPattern, ParseContext parseContext) {
		past = matchedPattern == 0;
		relative = parseContext.getParseMark() == 1;

		duration = (Expression<Duration>) expressions[0];
		if (relative)
			date = (Expression<SkriptDate>) expressions[1];
		return true;
	}

	@Override
	public SkriptDate[] getValues(TriggerContext ctx) {
		Duration dur = duration.getSingle(ctx);
		if (dur == null)
			return new SkriptDate[0];

		if (relative) {
			SkriptDate d = date.getSingle(ctx);
			if (d == null)
				return new SkriptDate[0];
			return new SkriptDate[] {past ? d.minus(dur) : d.plus(dur)};
		}
		return new SkriptDate[] {past ? SkriptDate.now().minus(dur) : SkriptDate.now().plus(dur)};
	}

	@Override
	public String toString(@Nullable TriggerContext ctx, boolean debug) {
		if (relative) {
			return duration.toString(ctx, debug) + (past ? " before date " : " after date ") + date.toString(ctx, debug);
		} else {
			return duration.toString(ctx, debug) + (past ? " in the past" : " in the future");
		}
	}
}