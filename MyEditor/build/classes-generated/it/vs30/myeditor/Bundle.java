package it.vs30.myeditor;
/** Localizable strings for {@link it.vs30.myeditor}. */
class Bundle {
    /**
     * @return <i>Export travel time...</i>
     * @see ExportTravelTime
     */
    static String CTL_ExportTravelTime() {
        return org.openide.util.NbBundle.getMessage(Bundle.class, "CTL_ExportTravelTime");
    }
    /**
     * @return <i>Save project</i>
     * @see saveProjectAs
     */
    static String CTL_saveProjectAs() {
        return org.openide.util.NbBundle.getMessage(Bundle.class, "CTL_saveProjectAs");
    }
    private Bundle() {}
}
