package github.tornaco.android.thanos.power;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;

import androidx.annotation.Nullable;

import github.tornaco.android.thanos.core.app.ThanosManager;
import github.tornaco.android.thanos.core.pm.Pkg;

public class ShortcutStubActivity extends Activity {

    private static final String EXTRA_TARGET_PKG = "stub.extra.pkg";
    private static final String EXTRA_TARGET_USER_ID = "stub.extra.userId";

    public static Intent createIntent(Context context, String targetPackage, int userId) {
        Intent intent = new Intent(context, ShortcutStubActivity.class);
        intent.putExtra(EXTRA_TARGET_PKG, targetPackage);
        intent.putExtra(EXTRA_TARGET_USER_ID, userId);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        return intent;
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        try {
            internalResolveIntent();
        } finally {
            finish();
        }
    }

    private void internalResolveIntent() {
        Intent intent = getIntent();
        if (intent == null) {
            return;
        }

        String target = intent.getStringExtra(EXTRA_TARGET_PKG);
        if (target == null) {
            Toast.makeText(this, "Target is null", Toast.LENGTH_SHORT).show();
            return;
        }

        int userId = intent.getIntExtra(EXTRA_TARGET_USER_ID, 0);

        ThanosManager.from(getApplicationContext())
                .ifServiceInstalled(thanosManager ->
                        thanosManager.getPkgManager().launchSmartFreezePkg(new Pkg(target, userId)));
    }
}
