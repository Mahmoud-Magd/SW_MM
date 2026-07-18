package pro.sketchware.utility;

import android.os.Environment;

import java.io.File;




// =========================================================
// FilePathUtil = File path resolver
// =========================================================

// PURPOSE:
    // Centralized resolver for every on-disk path used by a Sketchware
    // project (sc_id) and by locally imported libraries.
    // Callers should never build these paths by hand — go through here
    // so the on-disk layout only has to change in one place.

// LAYOUT (relative to SKETCHWARE_DATA):
    // sc_id/
        // compile_log
        // permission
        // import
        // proguard-rules.pro
        // local_library
        // converted-vectors/
        // java              (manifest copy)
        // broadcast         (manifest copy)
        // service           (manifest copy)
        // files/
            // assets
            // java
            // kt_plugins
            // resource
            // broadcast
            // service
            // native_libs
            // library/
                // jar
                // dex
                // res

// LAYOUT (relative to SKETCHWARE_LOCAL_LIBS):
    // libraryName/
        // classes.jar
        // classes.dex
        // res

// =========================================================

public class FilePathUtil {




    // =========================================================
    // CONSTANTS
    // =========================================================
    private static final File SKETCHWARE_DATA       = new File (Environment.getExternalStorageDirectory(), ".sketchware/data/");
    private static final File SKETCHWARE_LOCAL_LIBS = new File (Environment.getExternalStorageDirectory(), ".sketchware/libs/local_libs");




    // =========================================================
    // PUBLIC METHODS — ROOT {FILES}
    // =========================================================
    public static File localLibFolder (String libraryName) {
        return new File (SKETCHWARE_LOCAL_LIBS, libraryName);
    }
    
    public static File dataFolder (String sc_id) {
        return new File (SKETCHWARE_DATA, sc_id);
    }
        
        public static File filesFolder (String sc_id) {
            return new File (dataFolder (sc_id), "files");
        }
            
            public static File javaFolder (String sc_id) {
                return new File (filesFolder (sc_id), "java");
            }
            
            public static File filesLibraryFolder (String sc_id) {
                return new File (filesFolder (sc_id), "library");
            }
    
    
    
    
    
    // =========================================================
    // PUBLIC METHODS — SKETCHWARE_LOCAL_LIBS/libraryName
    // =========================================================
    public static String localLibFolderPath (String libraryName) {
        return localLibFolder (libraryName).getAbsolutePath();
    }
        
        public static String getJarPathLocalLibrary (String libraryName) {
            return new File (localLibFolder (libraryName), "classes.jar").getAbsolutePath();
        }
        
        public static String getDexPathLocalLibrary (String libraryName) {
            return new File (localLibFolder (libraryName), "classes.dex").getAbsolutePath();
        }
        
        public static String getResPathLocalLibrary (String libraryName) {
            return new File (localLibFolder (libraryName), "res").getAbsolutePath();
        }
    
    
    
    
    
    // =========================================================
    // PUBLIC METHODS — sc_id root {PATHS}
    // =========================================================
    public static String dataFolderPath (String sc_id) {
        return dataFolder (sc_id).getAbsolutePath();
    }
        
        public static String getLastCompileLogPath (String sc_id) {
            return new File (dataFolder (sc_id), "compile_log").getAbsolutePath();
        }
        
        public static String getPathPermission (String sc_id) {
            return new File (dataFolder (sc_id), "permission").getAbsolutePath();
        }
        
        public static String getPathImport (String sc_id) {
            return new File (dataFolder (sc_id), "import").getAbsolutePath();
        }
        
        public static String getPathProguard (String sc_id) {
            return new File (dataFolder (sc_id), "proguard-rules.pro").getAbsolutePath();
        }
        
        public static String getPathLocalLibrary (String sc_id) {
            return new File (dataFolder (sc_id), "local_library").getAbsolutePath();
        }
        
        public static String getPathSvg (String sc_id) {
            return new File (dataFolder (sc_id), "converted-vectors/").getAbsolutePath();
        }
        
        public static String getSvgFullPath (String sc_id, String resName) {
            return new File (getPathSvg (sc_id) + File.separator + resName + ".svg").getAbsolutePath();
        }
        
        // =========================================================
        // PUBLIC METHODS — sc_id root (manifest copies)
        // =========================================================
        // NOTE:
            // These mirror the "files/" versions below but live directly
            // under sc_id instead of sc_id/files. Not a typo — both exist
            // on disk and are used in different places.
    
        public static String getManifestJava (String sc_id) {
            return new File (dataFolder (sc_id), "java").getAbsolutePath();
        }
    
        public static String getManifestBroadcast (String sc_id) {
            return new File (dataFolder (sc_id), "broadcast").getAbsolutePath();
        }
    
        public static String getManifestService (String sc_id) {
            return new File (dataFolder (sc_id), "service").getAbsolutePath();
        }
        
        // =========================================================
        // PUBLIC METHODS — sc_id/files
        // =========================================================
        public static String filesFolderPath (String sc_id) {
            return filesFolder (sc_id).getAbsolutePath();
        }
            
            public static String getPathJava (String sc_id) {
                return javaFolder (sc_id).getAbsolutePath();
            }
            
            public static String getPathAssets (String sc_id) {
                return new File (filesFolder (sc_id), "assets").getAbsolutePath();
            }
            
            public static String getPathKotlinCompilerPlugins (String sc_id) {
                return new File (filesFolder (sc_id), "kt_plugins").getAbsolutePath();
            }
            
            public static String getPathResource (String sc_id) {
                return new File (filesFolder (sc_id), "resource").getAbsolutePath();
            }
            
            public static String getPathBroadcast (String sc_id) {
                return new File (filesFolder (sc_id), "broadcast").getAbsolutePath();
            }
            
            public static String getPathService (String sc_id) {
                return new File (filesFolder (sc_id), "service").getAbsolutePath();
            }
            
            public static String getPathNativelibs (String sc_id) {
                return new File (filesFolder (sc_id), "native_libs").getAbsolutePath();
            }
            
            // =========================================================
            // PUBLIC METHODS — sc_id/files/library (user-imported libs)
            // =========================================================
            
            public static String filesLibraryFolderPath (String sc_id) {
                return filesLibraryFolder (sc_id).getAbsolutePath();
            }
                
                public static String getJarPathLocalLibraryUser (String sc_id) {
                    return new File (filesLibraryFolder (sc_id), "jar").getAbsolutePath();
                }
            
                public static String getDexPathLocalLibraryUser (String sc_id) {
                    return new File (filesLibraryFolder (sc_id), "dex").getAbsolutePath();
                }
            
                public static String getResPathLocalLibraryUser (String sc_id) {
                    return new File (filesLibraryFolder (sc_id), "res").getAbsolutePath();
                }
    
    
    
    
    
}


