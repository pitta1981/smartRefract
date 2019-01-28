package it.vs30.geometryView;
/** Localizable strings for {@link it.vs30.geometryView}. */
class Bundle {
    /**
     * @return <i>geometryViewer</i>
     * @see geometryViewerTopComponent
     */
    static String CTL_geometryViewerAction() {
        return org.openide.util.NbBundle.getMessage(Bundle.class, "CTL_geometryViewerAction");
    }
    /**
     * @return <i>Geometry Window</i>
     * @see geometryViewerTopComponent
     */
    static String CTL_geometryViewerTopComponent() {
        return org.openide.util.NbBundle.getMessage(Bundle.class, "CTL_geometryViewerTopComponent");
    }
    /**
     * @return <i>This is a GeometryViewer window</i>
     * @see geometryViewerTopComponent
     */
    static String HINT_geometryViewerTopComponent() {
        return org.openide.util.NbBundle.getMessage(Bundle.class, "HINT_geometryViewerTopComponent");
    }
    private Bundle() {}
}
