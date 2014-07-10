package com.sky.dvdstore

import spock.lang.Specification
import spock.lang.Unroll

import static org.hamcrest.Matchers.equalTo
import static org.hamcrest.Matchers.hasProperty
import static spock.util.matcher.HamcrestSupport.expect
import static org.hamcrest.Matchers.is

class DvdServiceTest extends Specification {
    private DvdService dvdService;

    void setup() {
        dvdService = new DvdServiceImpl(new DvdRepositoryStub());
    }

    @Unroll
    def "retrieval of a dvd fails if dvdReference does not start with #correct_dvd_prefix prefix"() {
        given:
            String invalidDvdReference = "INVALID-TEXT"

        when:
            dvdService.retrieveDvd(invalidDvdReference)

        then:
            thrown(IllegalArgumentException)

        where:
            correct_dvd_prefix = Constants.DVD_REFERENCE_PREFIX
    }

    def "retrieval of non existent dvd throws DvdNotFoundException"() {
        given:
            String nonExistentDvdReference = "DVD-999"

        when:
            dvdService.retrieveDvd(nonExistentDvdReference);

        then:
            thrown(DvdNotFoundException)
    }

    def "retrieval of a dvd by it's reference returns a dvd that has given reference"() {
        given:
            String dvdReference = "DVD-TG423"

        when:
            Dvd dvd = dvdService.retrieveDvd(dvdReference)

        then:
            expect dvd, hasProperty("reference", equalTo(dvdReference))
    }

    @Unroll
    def "getting summary for a dvd fails if dvdReference does not start with #correct_dvd_prefix prefix"() {
        given:
            String invalidDvdReference = "INVALID-TEXT"

        when:
            dvdService.getDvdSummary(invalidDvdReference)

        then:
            thrown(IllegalArgumentException)

        where:
            correct_dvd_prefix = Constants.DVD_REFERENCE_PREFIX
    }

    def "getting summary for non existent dvd throws DvdNotFoundException"() {
        given:
            String nonExistentDvdReference = "DVD-999"

        when:
            dvdService.getDvdSummary(nonExistentDvdReference);

        then:
            thrown(DvdNotFoundException)
    }

    def "returned summary for a dvd contains dvd reference, title of the dvd and a review"() {
        given:
            String dvdReference = "DVD-TG423"

        when:
            String summary = dvdService.getDvdSummary(dvdReference);

        then:
            expect summary, is("[DVD-TG423] Topgun - All action film")
    }

    def "returned summary for a dvd with 10 word review is not shortened"() {
        given:
            String dvdReference = "DVD-LOTR123"

        when:
            String summary = dvdService.getDvdSummary(dvdReference);

        then:
            expect summary, is("[DVD-LOTR123] Lord of the Rings - Lot of trouble with a ring that has magical powers!")
    }

    def "returned summary for a dvd with long review (more than 10 words) contains 10 first words of the review followed by ..."() {
        given:
            String dvdReference = "DVD-S765"

        when:
            String summary = dvdService.getDvdSummary(dvdReference);

        then:
            expect summary, is("[DVD-S765] Shrek - Big green monsters, they're just all the rage these days...")
    }

}
