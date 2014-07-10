package com.sky.dvdstore;

import java.util.StringTokenizer;

public class DvdServiceImpl implements DvdService {

    private final DvdRepository dvdRepository;

    public DvdServiceImpl(DvdRepository dvdRepository) {
        this.dvdRepository = dvdRepository;
    }

    @Override
    public Dvd retrieveDvd(String dvdReference) throws DvdNotFoundException {
        if (!hasCorrectPrefix(dvdReference)) {
            throw new IllegalArgumentException("dvdReference should start with " + Constants.DVD_REFERENCE_PREFIX);
        }

        Dvd dvd = dvdRepository.retrieveDvd(dvdReference);

        if (dvd == null) {
            throw new DvdNotFoundException("Dvd with " + dvdReference + "was not found");
        }

        return dvd;
    }

    @Override
    public String getDvdSummary(String dvdReference) throws DvdNotFoundException {
        Dvd dvd = retrieveDvd(dvdReference);
        return constructSummary(dvd);
    }

    private static boolean hasCorrectPrefix(String dvdReference) {
        return dvdReference.startsWith(Constants.DVD_REFERENCE_PREFIX);
    }

    private static String constructSummary(Dvd dvd) {
        return String.format("[%s] %s - %s", dvd.getReference(), dvd.getTitle(), prepareReviewStr(dvd.getReview()));
    }

    private static String prepareReviewStr(String review) {

        StringTokenizer tokenizer = new StringTokenizer(review.trim(), " ");

        if (tokenizer.countTokens() <= 10) {
            return review;
        }

        String tenthToken = getTenthToken(tokenizer);

        return review.substring(0, review.indexOf(tenthToken) + tenthToken.length()).replaceAll("[^a-zA-Z]+$", "") + "...";
    }

    private static String getTenthToken(StringTokenizer tokenizer) {
        int wordCount = 0;
        while (tokenizer.hasMoreTokens()) {
            String str = tokenizer.nextToken();
            wordCount++;
            if (wordCount == 10) {
                return str;
            }
        }

        return "";
    }
}
