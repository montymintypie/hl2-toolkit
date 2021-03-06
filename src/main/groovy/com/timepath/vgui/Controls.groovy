package com.timepath.vgui

import com.timepath.vgui.controls.CExLabel

/**
 * @author TimePath
 */
class Controls {

    static HashMap<String, Class<? extends Control>> defaults = [
            "CExLabel"                             : CExLabel,
            "CIconPanel"                           : Control,
            "CTeamMenu"                            : Control,
            "CTFClassInfoPanel"                    : Control,
            "CTFArrowPanel"                        : Control,
            "CTFImagePanel"                        : Control,
            "CEmbeddedItemModelPanel"              : Control, // ooh, fancy
            "CTFHudTimeStatus"                     : Control,
            "CWaveStatusPanel"                     : Control, // MvM?
            "CControlPointCountdown"               : Control, // control points
            "CWaveCompleteSummaryPanel"            : Control, // MvM?
            "CTFTextWindow"                        : Control, // text motd?
            "CTFRichText"                          : Control,
            "CTankStatusPanel"                     : Control,
            "CExImageButton"                       : Control,
            "CEngyDestroyMenuItem"                 : Control,
            "CCurrencyStatusPanel"                 : Control,
            "CTFClassImage"                        : Control,
            "CInWorldCurrencyStatus"               : Control, // MvM in world currency
            "CTFClientScoreBoardDialog"            : Control, // scoreboard
            "CTFHudEscort"                         : Control, // payload?
            "CWarningSwoop"                        : Control,
            "CItemModelPanel"                      : Control,
            "CStoreItemControlsPanel"              : Control,
            "CExButton"                            : Control,
            "CStorePreviewItemPanel"               : Control,
            "CStorePricePanel"                     : Control,
            "CRichTextWithScrollbarBorders"        : Control,
            "CTFPlayerModelPanel"                  : Control,
            "CArmoryPanel"                         : Control,
            "CNotificationsPresentPanel"           : Control,
            "CEconItemDetailsRichText"             : Control,
            "CTFMapStampsInfoDialog"               : Control,
            "CStorePreviewItemIcon"                : Control,
            "CMouseMessageForwardingPanel"         : Control,
            "CGenericNotificationToast"            : Control,
            "CStorePreviewClassIcon"               : Control,
            "CNavigationPanel"                     : Control,
            "CAvatarImagePanel"                    : Control,
            "CNotificationQueuePanel"              : Control,
            "CPreviewRotButton"                    : Control,
            "CNotificationToastControl"            : Control,
            "CItemMaterialCustomizationIconPanel"  : Control,
            "CImagePanel"                          : Control,
            "CExplanationPopup"                    : Control,
            "CRGBAImagePanel"                      : Control,
            "CBackpackPanel"                       : Control,
            "CModePanel"                           : Control,
            "CTrainingDialog"                      : Control,
            "CAchievementsDialog"                  : Control,
            "CClassMenu"                           : Control,
            "CBitmapPanel"                         : Control,
            "CModeSelectionPanel"                  : Control,
            "CCustomTextureImagePanel"             : Control,
            "CTFClassTipsItemPanel"                : Control,
            "CBasicTraining_ClassSelectionPanel"   : Control,
            "CBasicTraining_ClassDetailsPanel"     : Control,
            "COfflinePractice_ModeSelectionPanel"  : Control,
            "COfflinePractice_MapSelectionPanel"   : Control,
            "CLoadoutPresetPanel"                  : Control,
            "CClassLoadoutPanel"                   : Control,
            "CBuildingHealthBar"                   : Control,
            "CBuildingStatusAlertTray"             : Control,
            "CTFFreezePanelHealth"                 : Control,
            "CTFTeamButton"                        : Control,
            "CModelPanel"                          : Control,
            "CTFFooter"                            : Control,
            "CMvMBombCarrierProgress"              : Control,
            "CTFProgressBar"                       : Control,
            "CVictorySplash"                       : Control,
            "CMvMVictoryPanelContainer"            : Control,
            "CMvMWaveLossPanel"                    : Control,
            "CExRichText"                          : Control,
            "CTFIntroMenu"                         : Control,
            "CTFVideoPanel"                        : Control,
            "CTFLayeredMapItemPanel"               : Control,
            "CTFClassTipsPanel"                    : Control,
            "CBaseModelPanel"                      : Control,
            "CCreditDisplayPanel"                  : Control,
            "CCreditSpendPanel"                    : Control,
            "CVictoryPanel"                        : Control,
            "CMvMVictoryMannUpPanel"               : Control,
            "CMvMVictoryMannUpEntry"               : Control,
            "CTFHudEscortProgressBar"              : Control,
            "CPublishFileDialog"                   : Control,
            "CPublishedFileBrowserDialog"          : Control,
            "CQuickPlayBusyDialog"                 : Control,
            "CQuickplayDialog"                     : Control,
            "CMainMenuNotificationsControl"        : Control,
            "CSteamWorkshopDialog"                 : Control,
            "CSteamWorkshopItemPanel"              : Control,
            "CTankProgressBar"                     : Control,
            "CPanelListPanel"                      : Control,
            "CTrainingItemPanel"                   : Control,
            "CTFTrainingComplete"                  : Control,
            "CImageButton"                         : Control,
            "CCommentaryExplanationDialog"         : Control,
            "CCommentaryItemPanel"                 : Control,
            "CTGAImagePanel"                       : Control,
            "COfflinePracticeServerPanel"          : Control,
            "CLoadGameDialog"                      : Control,
            "CNewGameDialog"                       : Control,
            "COptionsSubMultiplayer"               : Control,
            "CPlayerListDialog"                    : Control,
            "CVoteSetupDialog"                     : Control,
            "CCvarSlider"                          : Control,
            "CControllerMap"                       : Control,
            "CScenarioInfoPanel"                   : Control,
            "CTFButton"                            : Control,
            "CTFImageButton"                       : Control,
            "CTFFlagStatus"                        : Control,
            "CTFHudMannVsMachineScoreboard"        : Control,
            "CReplayReminderPanel"                 : Control,
            "CircularProgressBar"                  : Control, // what the hell is this?
            "PanelListPanel"                       : Control,
            "ImageButton"                          : Control,
            "RichText"                             : Control,
            "SectionedListPanel"                   : Control,
            "ListPanel"                            : Control,
            "RoundInfoOverlay"                     : Control,
            "ProgressBar"                          : Control,
            "Slider"                               : Control,
            "Divider"                              : Control,
            "AnalogBar"                            : Control,
            "FooterPanel"                          : Control,
            "AnimatingImagePanel"                  : Control,
            "RotatingProgressBar"                  : Control,
            "MaterialButton"                       : Control,
            "CustomTextureStencilGradientMapWidget": Control,
            "RadioButton"                          : Control,
            "ScrollableEditablePanel"              : Control,
            "CheckButton"                          : Control,
            "ComboBox"                             : Control,
            "ScrollBar"                            : Control,
            "Button"                               : Control,
            "Panel"                                : Control,
            "ImagePanel"                           : Control,
            "ContinuousProgressBar"                : Control,
            "Menu"                                 : Control,
            "EditablePanel"                        : Control,
            "Frame"                                : Control,
            "ScalableImagePanel"                   : Control,
            "Label"                                : Control,
            "HTML"                                 : Control,
            "TextEntry"                            : Control
    ];

}
