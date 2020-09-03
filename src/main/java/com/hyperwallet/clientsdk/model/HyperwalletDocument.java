package com.hyperwallet.clientsdk.model;

import com.fasterxml.jackson.annotation.JsonFilter;
import com.hyperwallet.clientsdk.util.HyperwalletJsonConfiguration;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;

@JsonFilter(HyperwalletJsonConfiguration.INCLUSION_FILTER)
@XmlRootElement
@XmlAccessorType(XmlAccessType.FIELD)
public class HyperwalletDocument {

    public static enum EIdentityVerificationType {
        DRIVERS_LICENSE,
        PASSPORT,
        GOVERNMENT_ID,
        UTILITY_BILL,
        BIRTH_CERTIFICATE,
        BANK_STATEMENT,
        TAX_RETURN,
        OTHER,
        ACCOUNT_OPENING_LETTER,
        PASS_SAVINGS_BOOK,
        BANK_CARD,
        CREDIT_CARD_STATEMENT,
        OFFICIAL_GOVERNMENT_LETTER,
        PROPERTY_TAX_ASSESSMENT,
        BUSINESS_REGISTRATION,
        INCORPORATION,
        OPERATING_AGREEMENT,
        LETTER_OF_AUTHORIZATION
    }

    public static enum EDocumentCategory {
        ADDRESS,
        IDENTIFICATION,
        BUSINESS,
        AUTHORIZATION
    }

    public static enum ECountryCode {
        AD, AE, AF, AG, AI, AL, AM, AN, AO, AQ, AR, AS, AT, AU, AW, AX, AZ, BA, BB, BD, BE, BF, BG, BH, BI, BJ, BL, BM, BN, BO, BQ, BR, BS, BT, BV,
        BW, BY, BZ, CA, CC, CD, CF, CG, CH, CI, CK, CL, CM, CN, CO, CR, CU, CV, CW, CX, CY, CZ, DE, DJ, DK, DM, DO, DZ, EC, EE, EG, EH, ER, ES, ET,
        FI, FJ, FK, FM, FO, FR, FX, GA, GB, GD, GE, GF, GG, GH, GI, GL, GM, GN, GP, GQ, GR, GS, GT, GU, GW, GY, HK, HM, HN, HR, HT, HU, ID, IE, IL,
        IM, IN, IO, IQ, IR, IS, IT, JE, JM, JO, JP, KE, KG, KH, KI, KM, KN, KP, KR, KW, KY, KZ, LA, LB, LC, LI, LK, LR, LS, LT, LU, LV, LY, MA, MC,
        MD, ME, MF, MG, MH, MK, ML, MM, MN, MO, MP, MQ, MR, MS, MT, MU, MV, MW, MX, MY, MZ, NA, NC, NE, NF, NG, NI, NL, NO, NP, NR, NU, NZ, OM, PA,
        PE, PF, PG, PH, PK, PL, PM, PN, PR, PS, PT, PW, PY, QA, RE, RO, RS, RU, RW, SA, SB, SC, SD, SE, SG, SH, SI, SJ, SK, SL, SM, SN, SO, SR, SS,
        ST, SV, SX, SY, SZ, TC, TD, TF, TG, TH, TJ, TK, TL, TM, TN, TO, TR, TT, TV, TW, TZ, UA, UG, UM, US, UY, UZ, VA, VC, VE, VG, VI, VN, VU, WF,
        WS, XK, YE, YT, YU, ZA, ZM, _O, ZW
    }

    public enum EKycDocumentVerificationStatus {
        VERIFIED,
        INVALID,
        NEW
    }

    private EDocumentCategory category;

    private EIdentityVerificationType type;

    private ECountryCode country;

    private EKycDocumentVerificationStatus status;

    public EDocumentCategory getCategory() {
        return category;
    }

    public void setCategory(EDocumentCategory category) {
        this.category = category;
    }

    public HyperwalletDocument category(EDocumentCategory category) {
        setCategory(category);
        return this;
    }

    public EIdentityVerificationType getType() {
        return type;
    }

    public void setType(EIdentityVerificationType type) {
        this.type = type;
    }

    public HyperwalletDocument type(EIdentityVerificationType type) {
        setType(type);
        return this;
    }

    public ECountryCode getCountry() {
        return country;
    }

    public void setCountry(ECountryCode country) {
        this.country = country;
    }

    public HyperwalletDocument country(ECountryCode country) {
        setCountry(country);
        return this;
    }

    public EKycDocumentVerificationStatus getStatus() {
        return status;
    }

    public void setStatus(EKycDocumentVerificationStatus status) {
        this.status = status;
    }

    public HyperwalletDocument status(EKycDocumentVerificationStatus status) {
        setStatus(status);
        return this;
    }

}
