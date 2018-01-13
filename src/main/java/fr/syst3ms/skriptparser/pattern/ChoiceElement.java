package fr.syst3ms.skriptparser.pattern;

import java.util.List;

public class ChoiceElement {
    private PatternElement element;
    private int parseMark;

    public ChoiceElement(PatternElement element, int parseMark) {
        this.element = element;
        this.parseMark = parseMark;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null || !(obj instanceof ChoiceElement)) {
            return false;
        } else {
            ChoiceElement other = (ChoiceElement) obj;
            return element.equals(other.element) && parseMark == other.parseMark;
        }
    }
}
