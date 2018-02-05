package org.amv.access.sdk.sample.logic;

import org.amv.access.sdk.spi.certificate.AccessCertificatePair;
import org.amv.access.sdk.spi.error.AccessSdkException;

import java.util.List;

public interface ICertificatesView {
    /**
     * Called when initialization finished successfully.
     */
    void onInitializeFinished();

    /**
     * Called when initialization failed
     *
     * @param error the initialization error
     */
    void onInitializeFailed(AccessSdkException error);

    /**
     * Called when certificates were downloaded and stored.
     *
     * @param certificates the certificates that were downloaded.
     */
    void onCertificatesDownloaded(List<AccessCertificatePair> certificates);

    /**
     * Called when certificates download failed.
     *
     * @param error the download networkError
     */
    void onCertificatesDownloadFailed(AccessSdkException error);

    /**
     * Called when certificate revoke succeeded.
     *
     * @param certificates the current certificates
     */
    void onCertificateRevoked(List<AccessCertificatePair> certificates);

    /**
     * Called when certificate revoke failed.
     *
     * @param error the revoke error
     */
    void onCertificateRevokeFailed(AccessSdkException error);
}
