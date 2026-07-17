package pro.sketchware.lib.validator;

import android.content.Context;

import com.google.android.material.textfield.TextInputLayout;

import a.a.a./*BaseValidatorW*/ MB;




// =========================================================
// /*PackageNameValidatorW*/ PackageNameValidator = Package Name Validator (Watcher)
// =========================================================

// PURPOSE:
    // Thin TextWatcher/View glue over PkgNameValidator.
    // All validation logic AND message resolution live in PkgNameValidator.
    // This class only wires the watcher to
        // a TextInputLayout and forwards Context + strResId through.

// RENAME NOTE:
    // Renamed from PackageNameValidator → PackageNameValidatorW,
    // "W" for Watcher, to disambiguate from the logic-holding PkgNameValidator class.

// USAGE:
    // Prefer the TIL-only constructor:
        // new PackageNameValidator (myTil);
    // The Context+TIL constructor still exists only because MB
    // itself still exposes it — see MB's own TODO block.

// =========================================================

// TODO:
    // ======= CONSTRUCTOR =======
        // DELETE the "PackageNameValidator (Context ..., TextInputLayout ...) {...}"
            // constructor below once MB's own deprecated
            // Context+TextInputLayout constructor is deleted.
            // Kept only in lockstep with MB right now.

        // Please use "PackageNameValidator (TextInputLayout ...) {...}" instead
            // Context is pulled from the TIL via inherited getCtx(),
            // so there's no reason to pass it separately.

// =========================================================

public class PackageNameValidator extends /*BaseValidatorW*/ MB {




    // =========================================================
    // CONSTRUCTOR
    // =========================================================
    public PackageNameValidator (TextInputLayout textInputLayout) { super (textInputLayout); }

    /*TODO: DELETE THIS ↓↓↓*/
    // Mirrors MB's own deprecated Context+TIL constructor.
    // Delete this one the same day MB's is deleted — see TODO above.
    /** @deprecated Use {@link #PackageNameValidator(TextInputLayout)} instead. */
    @Deprecated (since = "7.0.0", forRemoval = true)
    public PackageNameValidator (Context context, TextInputLayout textInputLayout) {
        super (context, textInputLayout);
    }
    /*TODO: DELETE THIS ↑↑↑*/




    // =========================================================
    // TextWatcher OVERRIDE
    // =========================================================

    @Override
    public void onTextChanged (CharSequence s, int start, int before, int count) {
        PkgNameValidator.ValidationResult result = PkgNameValidator.validate (s);
        
        boolean isValid = result.isValid();
        getTil().setErrorEnabled ( ! isValid );

        
        /*MB.isValid*/ d = isValid;

        if ( ! isValid )
            getTil().setError (
                PkgNameValidator.buildCombinedMessage ( getCtx(), result.getIssues(), getStrResId() )
            );
    }




}


