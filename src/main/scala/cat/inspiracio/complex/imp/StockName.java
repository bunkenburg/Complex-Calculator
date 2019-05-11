package cat.inspiracio.complex.imp;

import java.nio.CharBuffer;
import java.util.Formatter;
import java.util.Formattable;
import java.util.Locale;
import static java.util.FormattableFlags.*;

public class StockName implements Formattable {

    private String symbol, companyName, frenchCompanyName;

    public StockName(String symbol, String companyName, String frenchCompanyName) { }

    /** @param flags UPPERCASE ALTERNATE LEFT_JUSTIFY */
    public void formatTo(Formatter fmt, int flags, int width, int precision) {

        boolean alternate = (flags & ALTERNATE) == ALTERNATE;
        boolean left = (flags & LEFT_JUSTIFY) == LEFT_JUSTIFY;
        boolean upper = (flags & UPPERCASE) == UPPERCASE;

        StringBuilder sb = new StringBuilder();

        // decide form of name
        String name = companyName;
        if (fmt.locale().equals(Locale.FRANCE))
            name = frenchCompanyName;
        boolean usesymbol = alternate || (precision != -1 && precision < 10);
        String out = (usesymbol ? symbol : name);

        // apply precision
        if (precision == -1 || out.length() < precision) {
            // write it all
            sb.append(out);
        } else {
            sb.append(out.substring(0, precision - 1)).append('*');
        }

        // apply width and justification
        int len = sb.length();
        if (len < width)
            for (int i = 0; i < width - len; i++)
                if ((flags & LEFT_JUSTIFY) == LEFT_JUSTIFY)
                    sb.append(' ');
                else
                    sb.insert(0, ' ');
                fmt.format(sb.toString());
    }

    public String toString() {
        return String.format("%s - %s", symbol, companyName);
    }

}