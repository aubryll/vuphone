
// Include your fully-qualified package statement.
package org.vuphone.wwatch.android.bindings;

import org.vuphone.wwatch.android.bindings.ISettingsViewCallback;

// Declare the interface.
interface IRegister {
    
    // Register a callback
    void registerCallback(in ISettingsViewCallback callback);
    
    // Unregister the callback
    void unregisterCallback(in ISettingsViewCallback callback);
}