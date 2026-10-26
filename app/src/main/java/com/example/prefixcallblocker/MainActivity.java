package com.example.prefixcallblocker;

import android.app.Activity;
import android.app.role.RoleManager;
import android.content.Context;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Build;
import android.os.Bundle;
import android.view.Gravity;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

public class MainActivity extends Activity {
    private TextView status;
    private static final int REQUEST_SCREENING_ROLE = 1001;

    @Override protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        buildUi();
        refreshStatus();
    }

    private void buildUi() {
        int pad = dp(24);
        LinearLayout root = new LinearLayout(this);
        root.setOrientation(LinearLayout.VERTICAL);
        root.setPadding(pad, pad, pad, pad);
        root.setGravity(Gravity.CENTER_VERTICAL);
        root.setBackgroundColor(0xFFFFFFFF);

        TextView title = new TextView(this);
        title.setText("Prefix Call Blocker");
        title.setTextSize(27);
        title.setTypeface(Typeface.DEFAULT, Typeface.BOLD);
        title.setTextColor(0xFF172033);
        root.addView(title, matchWrap());

        TextView prefix = new TextView(this);
        prefix.setText("Calls starting with\n+911409\nwill be automatically rejected.");
        prefix.setTextSize(19);
        prefix.setLineSpacing(dp(5), 1f);
        prefix.setTextColor(0xFF30394B);
        LinearLayout.LayoutParams prefixParams = matchWrap();
        prefixParams.topMargin = dp(22);
        root.addView(prefix, prefixParams);

        status = new TextView(this);
        status.setTextSize(15);
        status.setTextColor(0xFF4B5563);
        LinearLayout.LayoutParams statusParams = matchWrap();
        statusParams.topMargin = dp(24);
        root.addView(status, statusParams);

        Button enable = new Button(this);
        enable.setText("Enable call blocking");
        LinearLayout.LayoutParams buttonParams = matchWrap();
        buttonParams.topMargin = dp(18);
        root.addView(enable, buttonParams);
        enable.setOnClickListener(v -> requestScreeningRole());

        TextView note = new TextView(this);
        note.setText("Android requires you to approve this app as the call-screening app. The app does not upload or store your call data.");
        note.setTextSize(13);
        note.setTextColor(0xFF6B7280);
        LinearLayout.LayoutParams noteParams = matchWrap();
        noteParams.topMargin = dp(20);
        root.addView(note, noteParams);
        setContentView(root);
    }

    private void requestScreeningRole() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            RoleManager roleManager = (RoleManager) getSystemService(Context.ROLE_SERVICE);
            if (roleManager != null && roleManager.isRoleAvailable(RoleManager.ROLE_CALL_SCREENING)) {
                if (roleManager.isRoleHeld(RoleManager.ROLE_CALL_SCREENING)) {
                    refreshStatus();
                } else {
                    startActivityForResult(roleManager.createRequestRoleIntent(RoleManager.ROLE_CALL_SCREENING), REQUEST_SCREENING_ROLE);
                }
            } else {
                status.setText("Call screening is not available on this device.");
            }
        }
    }

    @Override protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_SCREENING_ROLE) refreshStatus();
    }

    @Override protected void onResume() { super.onResume(); refreshStatus(); }

    private void refreshStatus() {
        if (status == null) return;
        RoleManager rm = (RoleManager) getSystemService(Context.ROLE_SERVICE);
        boolean enabled = Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q && rm != null && rm.isRoleHeld(RoleManager.ROLE_CALL_SCREENING);
        status.setText(enabled ? "Status: ENABLED — matching calls will be rejected." : "Status: NOT ENABLED — tap the button and approve the role.");
        status.setTextColor(enabled ? 0xFF137547 : 0xFF9A5B00);
    }

    private LinearLayout.LayoutParams matchWrap() {
        return new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
    }
    private int dp(int value) { return (int) (value * getResources().getDisplayMetrics().density + 0.5f); }
}
