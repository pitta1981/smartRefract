package it.vs30.sidebar;
/** Localizable strings for {@link it.vs30.sidebar}. */
class Bundle {
    /**
     * @return <i>sideTools_</i>
     * @see sideTools_TopComponent
     */
    static String CTL_sideTools_Action() {
        return org.openide.util.NbBundle.getMessage(Bundle.class, "CTL_sideTools_Action");
    }
    /**
     * @return <i>sideTools_ Window</i>
     * @see sideTools_TopComponent
     */
    static String CTL_sideTools_TopComponent() {
        return org.openide.util.NbBundle.getMessage(Bundle.class, "CTL_sideTools_TopComponent");
    }
    /**
     * @return <i>This is a sideTools_ window</i>
     * @see sideTools_TopComponent
     */
    static String HINT_sideTools_TopComponent() {
        return org.openide.util.NbBundle.getMessage(Bundle.class, "HINT_sideTools_TopComponent");
    }
    private Bundle() {}
}
