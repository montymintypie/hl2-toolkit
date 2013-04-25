package com.timepath.hl2.io;

import com.timepath.hl2.io.util.HudFont;
import com.timepath.steam.io.VDF;
import com.timepath.steam.io.util.VDFNode;
import com.timepath.swing.TreeUtils;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.RandomAccessFile;
import java.util.HashMap;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.TreeNode;

/**
 *
 * TODO: Threading. This class can probably be executed as a thread.
 * If there are multiple values with platform tags, all the values become the last loaded value tag, but only if the variable is recognized
 *
 * Some tags:
 * $WINDOWS
 * $WIN32
 * $X360
 * $POSIX
 * $OSX
 * $LINUX
 * 
 * https://code.google.com/p/hl2sb-src/source/browse/#svn%2Ftrunk%2Fsrc%2Fgame%2Fclient%2Fgame_controls
 *
 * @author timepath
 */
public class RES extends VDF {

    private static final Logger LOG = Logger.getLogger(RES.class.getName());

    private String hudFolder;

    public RES(String hudFolder) {
        this.hudFolder = hudFolder;
    }

    public static final HashMap<String, HudFont> fonts = new HashMap<String, HudFont>();

    // TODO: Special exceptions for *scheme.res, hudlayout.res,
    public static void analyze(final File file, final DefaultMutableTreeNode top) {
        if(file.isDirectory()) {
            return;
        }
        
        VDFNode pseudo = new VDFNode();

        Scanner s = null;
        try {
            RandomAccessFile rf = new RandomAccessFile(file.getPath(), "r");
            s = new Scanner(rf.getChannel());
            processAnalyze(s, pseudo, file);
            if(file.getName().equalsIgnoreCase("ClientScheme.res")) {
//                clientScheme(pseudo);
            }
        } catch(FileNotFoundException ex) {
            LOG.log(Level.SEVERE, null, ex);
        } finally {
            if(s != null) {
                s.close();
            }
        }
        
        TreeUtils.moveChildren(pseudo, top);
    }

    private static void clientScheme(DefaultMutableTreeNode props) {
        LOG.info("Found clientscheme");
        if(!(props instanceof VDFNode)) {
            LOG.log(Level.WARNING, "TreeNode not instanceof VDFNode", props);
            return;
        }
        VDFNode root = ((VDFNode) props).get("Scheme").get("Fonts");
        for(int i = 0; i < root.getChildCount(); i++) {
            TreeNode node = root.getChildAt(i);
            if(!(node instanceof VDFNode)) {
                continue;
            }
            VDFNode fontNode = (VDFNode) node;
            VDFNode detailNode = null;
            for(int j = 0; j < fontNode.getChildCount(); j++) {
                TreeNode detail = fontNode.getChildAt(j);
                if(!(detail instanceof VDFNode)) {
                    continue;
                }
                detailNode = (VDFNode) detail;
                break; // XXX: hardcoded detail level (the first one)
            }
            if(detailNode == null) {
                continue;
            }
            String fontName = fontNode.getKey();
//            fonts.put(fontName, new HudFont(fontName, detailFont));
            LOG.log(Level.INFO, "TODO: Load font {0}", fontName);
        }
        LOG.info("Loaded clientscheme");
    }
    
}