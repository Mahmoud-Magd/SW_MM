package pro.sketchware.lib.validator;

import java.io.File;
import java.util.LinkedHashSet;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import a.a.a.uq;




// =========================================================
// PkgNameValidator = Package Name Validator (pure logic)
// =========================================================

// PURPOSE:
    // Pure validation logic for package names, extracted out of
    // PackageNameValidatorW so the rules can be unit-tested and
    // reused without an Android View / TextWatcher attached.

// BEHAVIOR — CHANGED FROM ORIGINAL:
    // The original PackageNameValidator stopped at the first failed
    // check (early-return chain). This version runs EVERY check
    // regardless of earlier failures and returns the full combined
    // list of issues, so the caller can show the user everything
    // wrong with the input in one pass.

// CHECKS PERFORMED (all run every time, in this fixed order):
    // 1. EMPTY             — trimmed input has zero length (short-circuits, no other checks make sense)
    // 2. TOO_LONG           — trimmed length > MAX_LENGTH
    // 3. STARTS_WITH_DOT    — input starts with '.'
    // 4. INVALID_CHARS      — any character outside [a-zA-Z0-9.]
    // 5. RESERVED_KEYWORD   — any alphanumeric token equals a word in uq.b
    // 6. CONSECUTIVE_DOTS   — input contains ".."
    // 7. MISSING_DOT        — input contains no '.' at all
    // 8. ENDS_WITH_DOT      — input ends with '.'

// NOTE ON RESERVED_KEYWORD / INVALID_CHARS TOKENIZING:
    // Reserved-word matching splits on ANY non-alphanumeric run
    // (not just dots), so stray characters like spaces don't hide
    // a reserved word from detection. e.g. ". public.my.pkg."
    // still flags "public" even with the leading ". " in front of it.

// USAGE:
    // ValidationResult r = PkgNameValidator.validate ("com.example.app");
    // ValidationResult r = PkgNameValidator.validateFile (someFile);
    // ValidationResult r = PkgNameValidator.validatePath ("/src/com/example/App.java");
    // r.isValid();
    // r.getIssues();  // full list, empty if valid

// =========================================================

public final class PkgNameValidator {




    // =========================================================
    // CONSTANTS
    // =========================================================

    public static final int MAX_LENGTH = 50;

    // Any character NOT in this set is considered invalid.
    private static final String VALID_CHARS_REGEX = "[a-zA-Z0-9.]";

    // Splits on any run of non-alphanumeric characters — used to
    // tokenize for reserved-word lookup independent of dot structure.
    private static final String TOKEN_SPLIT_REGEX = "[^a-zA-Z0-9]+";




    // =========================================================
    // CONSTRUCTOR
    // =========================================================

    // Static-only utility class — no instances.
    private PkgNameValidator() {}




    // =========================================================
    // PUBLIC METHODS — validation
    // =========================================================

    // Validates a raw package name string, collecting ALL issues found.
    public static ValidationResult validate (CharSequence packageName) {
        String input = (packageName == null) ? "" : packageName.toString().trim();

        List<Issue> issues = new ArrayList<>();

        if ( input.isEmpty() ) {
            issues.add ( new Issue (Reason.EMPTY, null) );
            return new ValidationResult (issues);
        }

        if ( input.length() > MAX_LENGTH ) {
            issues.add ( new Issue (Reason.TOO_LONG, null) );
        }

        if ( input.startsWith (".") ) {
            issues.add ( new Issue (Reason.STARTS_WITH_DOT, null) );
        }

        String invalidChars = findInvalidChars (input);
        if (invalidChars != null) {
            issues.add ( new Issue (Reason.INVALID_CHARS, invalidChars) );
        }

        String reservedWords = findReservedWords (input);
        if (reservedWords != null) {
            issues.add ( new Issue (Reason.RESERVED_KEYWORD, reservedWords) );
        }

        if ( input.contains ("..") ) {
            issues.add ( new Issue (Reason.CONSECUTIVE_DOTS, null) );
        }

        if ( ! input.contains (".") ) {
            issues.add ( new Issue (Reason.MISSING_DOT, null) );
        }

        if ( input.endsWith (".") ) {
            issues.add ( new Issue (Reason.ENDS_WITH_DOT, null) );
        }

        return new ValidationResult (issues);
    }

    // Converts a File into a package-name candidate, then validates it.
    public static ValidationResult validateFile (File file) {
        return validate ( toPackageName (file) );
    }

    // Converts a file-path String into a package-name candidate, then validates it.
    public static ValidationResult validatePath (String filePath) {
        return validate ( toPackageName (filePath) );
    }




    // =========================================================
    // PUBLIC METHODS — conversion
    // =========================================================

    // Turns a File's path into a dotted package-name candidate.
    // Strips the extension, normalizes separators, converts "/" to ".".
    public static String toPackageName (File file) {
        return (file == null) ? "" : toPackageName ( file.getPath() );
    }

    public static String toPackageName (String filePath) {
        if (filePath == null || filePath.isEmpty()) return "";

        String stripped   = stripExtension (filePath);
        String normalized = stripped.replace ('\\', '/');

        while ( normalized.startsWith ("/") ) normalized = normalized.substring (1);
        while ( normalized.endsWith ("/") )   normalized = normalized.substring (0, normalized.length() - 1);

        return normalized.replace ('/', '.');
    }




    // =========================================================
    // PRIVATE METHODS
    // =========================================================

    private static String stripExtension (String path) {
        int lastDot   = path.lastIndexOf ('.');
        int lastSlash = Math.max ( path.lastIndexOf ('/'), path.lastIndexOf ('\\') );

        return (lastDot > lastSlash) ? path.substring (0, lastDot) : path;
    }

    // Returns a comma-joined string of distinct invalid characters found, or null if none.
    private static String findInvalidChars (String input) {
        Set<Character> invalid = new LinkedHashSet<> ();

        for ( int i = 0; i < input.length(); i++ ) {
            char c = input.charAt (i);
            if ( ! String.valueOf (c).matches (VALID_CHARS_REGEX) ) {
                invalid.add (c);
            }
        }

        if ( invalid.isEmpty() ) return null;

        StringBuilder sb = new StringBuilder();
        for ( Character c : invalid ) {
            if ( sb.length() > 0 ) sb.append (", ");
            sb.append (c);
        }
        return sb.toString();
    }

    // Returns a comma-joined string of distinct reserved words found, or null if none.
    // Tokenizes on ANY non-alphanumeric run, not just dots.
    private static String findReservedWords (String input) {
        Set<String> found = new LinkedHashSet<> ();

        for ( String token : input.split (TOKEN_SPLIT_REGEX) ) {
            if ( token.isEmpty() ) continue;

            for ( String reserved : uq.b ) {
                if ( reserved.equals (token) ) {
                    found.add (token);
                    break;
                }
            }
        }

        if ( found.isEmpty() ) return null;

        StringBuilder sb = new StringBuilder();
        for ( String w : found ) {
            if ( sb.length() > 0 ) sb.append (", ");
            sb.append (w);
        }
        return sb.toString();
    }




    // =========================================================
    // REASON — machine-readable failure category
    // =========================================================

    public enum Reason {
        EMPTY,
        TOO_LONG,
        STARTS_WITH_DOT,
        INVALID_CHARS,
        RESERVED_KEYWORD,
        CONSECUTIVE_DOTS,
        MISSING_DOT,
        ENDS_WITH_DOT
    }




    // =========================================================
    // ISSUE — a single validation failure
    // =========================================================

    public static final class Issue {

        private final Reason reason;
        private final String detail; // e.g. offending chars/words, joined; null if not applicable

        private Issue (Reason reason, String detail) {
            this.reason = reason;
            this.detail = detail;
        }

        public Reason getReason() { return reason; }
        public String getDetail() { return detail; }
    }




    // =========================================================
    // VALIDATION RESULT — full outcome, all issues combined
    // =========================================================

    public static final class ValidationResult {

        private final List<Issue> issues;

        private ValidationResult (List<Issue> issues) {
            this.issues = issues;
        }

        public boolean isValid()       { return issues.isEmpty(); }
        public List<Issue> getIssues() { return issues; }
    }




}


