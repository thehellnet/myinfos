package org.thehellnet.mobile.myinfos.cell;

import android.os.Build;
import android.telephony.CellInfo;
import android.telephony.CellInfoCdma;
import android.telephony.CellInfoGsm;
import android.telephony.CellInfoLte;
import android.telephony.CellInfoWcdma;

import org.thehellnet.mobile.myinfos.utility.CellUtility;

import java.util.Locale;

/**
 * Created by sardylan on 16/02/17.
 */

public final class CellDataFactory {

    public static CellData parseData(CellInfo cellInfo) {
        if (cellInfo instanceof CellInfoGsm) {
            return parseCellGsm((CellInfoGsm) cellInfo);
        } else if (cellInfo instanceof CellInfoCdma) {
            return parseCellCdma((CellInfoCdma) cellInfo);
        } else if (cellInfo instanceof CellInfoWcdma) {
            return parseCellWcdma((CellInfoWcdma) cellInfo);
        } else if (cellInfo instanceof CellInfoLte) {
            return parseCellLte((CellInfoLte) cellInfo);
        } else {
            return null;
        }
    }

    private static CellData parseCellGsm(CellInfoGsm cellInfo) {
        CellData cellData = new CellData(CellData.Type.GSM);
        cellData.dbm = cellInfo.getCellSignalStrength().getDbm();
        cellData.code = String.format(Locale.US, "%d-%d",
                cellInfo.getCellIdentity().getMcc(),
                cellInfo.getCellIdentity().getMnc());
        return cellData;
    }

    private static CellData parseCellCdma(CellInfoCdma cellInfo) {
        CellData cellData = new CellData(CellData.Type.CDMA);
        cellData.dbm = cellInfo.getCellSignalStrength().getDbm();
        cellData.code = String.format(Locale.US, "%d-%d-%d",
                cellInfo.getCellIdentity().getNetworkId(),
                cellInfo.getCellIdentity().getSystemId(),
                cellInfo.getCellIdentity().getBasestationId());
        cellData.latitude = CellUtility.parsePosition(cellInfo.getCellIdentity().getLatitude());
        cellData.longitude = CellUtility.parsePosition(cellInfo.getCellIdentity().getLongitude());
        return cellData;
    }

    private static CellData parseCellWcdma(CellInfoWcdma cellInfo) {
        CellData cellData = new CellData(CellData.Type.WCDMA);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR2) {
            cellData.dbm = cellInfo.getCellSignalStrength().getDbm();
            cellData.code = String.format(Locale.US, "%d-%d-%d",
                    cellInfo.getCellIdentity().getMcc(),
                    cellInfo.getCellIdentity().getMnc(),
                    cellInfo.getCellIdentity().getLac());
        }
        return cellData;
    }

    private static CellData parseCellLte(CellInfoLte cellInfo) {
        CellData cellData = new CellData(CellData.Type.LTE);
        cellData.dbm = cellInfo.getCellSignalStrength().getDbm();
        cellData.code = String.format(Locale.US, "%d-%d",
                cellInfo.getCellIdentity().getPci(),
                cellInfo.getCellIdentity().getTac());
        return cellData;
    }
}
