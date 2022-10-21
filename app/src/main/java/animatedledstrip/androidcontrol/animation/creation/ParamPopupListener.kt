package animatedledstrip.androidcontrol.animation.creation

import animatedledstrip.androidcontrol.animation.creation.popup.*

interface ParamPopupListener : IntEditPopup.IntEditListener,
    DoubleEditPopup.DoubleEditListener,
    DistanceEditPopup.DistanceEditListener,
    IDEditPopup.IDEditListener,
    LocationEditPopup.LocationEditListener,
    RotationEditPopup.RotationEditListener
