package org.amv.access.sdk.sample.logic;

import android.content.Context;

import org.amv.access.sdk.spi.certificate.AccessCertificatePair;
import org.amv.access.sdk.spi.certificate.DeviceCertificate;

import io.reactivex.Observable;

public interface ICertificatesController {
    /**
     * Initialize the controller before using any other functionality.
     *
     * @param context the application context
     * @param view    the view
     */
    void initialize(ICertificatesView view, Context context);

    /**
     * @return The device certificate.
     */
    Observable<DeviceCertificate> getDeviceCertificate();

    /**
     * Certificates are downloaded and stored
     */
    void downloadCertificates();

    /**
     * @return Previously downloaded certificates
     */
    Observable<AccessCertificatePair> getAccessCertificates();

    /**
     * Revoke a certificate
     *
     * @param certificate the certificate that will be revoked
     */
    void revokeCertificate(AccessCertificatePair certificate);
}
