package io.github.syst3ms.skriptparser.sections;

import io.github.syst3ms.skriptparser.Parser;
import io.github.syst3ms.skriptparser.file.FileSection;
import io.github.syst3ms.skriptparser.lang.Expression;
import io.github.syst3ms.skriptparser.lang.Statement;
import io.github.syst3ms.skriptparser.lang.TriggerContext;
import io.github.syst3ms.skriptparser.lang.Variable;
import io.github.syst3ms.skriptparser.lang.control.SelfReferencing;
import io.github.syst3ms.skriptparser.lang.lambda.ReturnSection;
import io.github.syst3ms.skriptparser.log.ErrorType;
import io.github.syst3ms.skriptparser.log.SkriptLogger;
import io.github.syst3ms.skriptparser.parsing.ParseContext;
import io.github.syst3ms.skriptparser.parsing.ParserState;
import io.github.syst3ms.skriptparser.types.changers.ChangeMode;
import io.github.syst3ms.skriptparser.util.Pair;
import org.jetbrains.annotations.Nullable;

import java.util.*;

public class SecFlatMap extends ReturnSection<Object> implements SelfReferencing {
    static {
        Parser.getMainRegistration().addSection(
                SecFlatMap.class,
                "flat map %~objects%|map %~objects% flat"
        );
    }

    private Expression<?> flatMapped;

	@Nullable
	private Statement actualNext;
	@Nullable
	private Iterator<?> iterator;
	private final List<Object> result = new ArrayList<>();

	@Override
	public boolean loadSection(FileSection section, ParserState parserState, SkriptLogger logger) {
		var currentLine = logger.getLine();
		super.setNext(this);
		return super.loadSection(section, parserState, logger) && checkReturns(logger, currentLine, true);
	}

    @Override
    public boolean init(Expression<?>[] expressions, int matchedPattern, ParseContext parseContext) {
        flatMapped = expressions[0];
        var logger = parseContext.getLogger();
        if (flatMapped.acceptsChange(ChangeMode.SET).isEmpty()
				|| flatMapped.acceptsChange(ChangeMode.DELETE).isEmpty()) {
            logger.error(
                    "The expression '" +
                            flatMapped.toString(TriggerContext.DUMMY, logger.isDebug()) +
                            "' cannot be changed",
                    ErrorType.SEMANTIC_ERROR
            );
            return false;
        }
        return true;
    }

    @SuppressWarnings("unchecked")
	@Override
    public Optional<? extends Statement> walk(TriggerContext ctx) {
		boolean isVariable = flatMapped instanceof Variable<?>;
		if (iterator == null)
			iterator = isVariable
					? ((Variable<?>) flatMapped).variablesIterator(ctx)
					: flatMapped.iterator(ctx);

		if (iterator.hasNext()) {
			setArguments(isVariable
					? ((Pair<String, Object>) iterator.next()).getSecond()
					: iterator.next()
			);
			return start();
		} else {
			if (result.size() == 0) {
				flatMapped.change(ctx, new Object[0], ChangeMode.DELETE);
			} else {
				flatMapped.change(ctx, result.toArray(), ChangeMode.SET);
			}
			finish();
			return Optional.ofNullable(actualNext);
		}
    }

	@Override
	public void step(Statement item) {
		assert getArguments().length == 1;
		var returned = getReturned().orElseThrow(AssertionError::new);
		result.addAll(Arrays.asList(returned)); // We add the filtered argument to the result
	}

	@Override
	public void finish() {
		// Cache clearing
		iterator = null;
		result.clear();
	}

    @Override
    public Class<?> getReturnType() {
        return flatMapped.getReturnType();
    }

    @Override
    public boolean isSingle() {
        return false;
    }

	@Override
	public Statement setNext(@Nullable Statement next) {
		actualNext = next;
		return this;
	}

	@Override
	public Optional<Statement> getActualNext() {
		return Optional.ofNullable(actualNext);
	}

	@Override
    public String toString(TriggerContext ctx, boolean debug) {
        return "flat map " + flatMapped.toString(ctx, debug);
    }
}
