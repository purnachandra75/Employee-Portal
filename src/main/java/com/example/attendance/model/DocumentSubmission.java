package com.example.attendance.model;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "document_submissions")
public class DocumentSubmission {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @Column(name = "tenth_file_name")
    private String tenthFileName;

    @Lob
    @Basic(fetch = FetchType.LAZY)
    @Column(name = "tenth_file_data", columnDefinition = "LONGBLOB")
    private byte[] tenthFileData;

    @Column(name = "tenth_content_type")
    private String tenthContentType;

    @Column(name = "twelfth_file_name")
    private String twelfthFileName;

    @Lob
    @Basic(fetch = FetchType.LAZY)
    @Column(name = "twelfth_file_data", columnDefinition = "LONGBLOB")
    private byte[] twelfthFileData;

    @Column(name = "twelfth_content_type")
    private String twelfthContentType;

    @Column(name = "pc_file_name")
    private String pcFileName;

    @Lob
    @Basic(fetch = FetchType.LAZY)
    @Column(name = "pc_file_data", columnDefinition = "LONGBLOB")
    private byte[] pcFileData;

    @Column(name = "pc_content_type")
    private String pcContentType;

    @Column(name = "od_file_name")
    private String odFileName;

    @Lob
    @Basic(fetch = FetchType.LAZY)
    @Column(name = "od_file_data", columnDefinition = "LONGBLOB")
    private byte[] odFileData;

    @Column(name = "od_content_type")
    private String odContentType;

    @Column(name = "pan_file_name")
    private String panFileName;

    @Lob
    @Basic(fetch = FetchType.LAZY)
    @Column(name = "pan_file_data", columnDefinition = "LONGBLOB")
    private byte[] panFileData;

    @Column(name = "pan_content_type")
    private String panContentType;

    @Column(name = "aadhaar_file_name")
    private String aadhaarFileName;

    @Lob
    @Basic(fetch = FetchType.LAZY)
    @Column(name = "aadhaar_file_data", columnDefinition = "LONGBLOB")
    private byte[] aadhaarFileData;

    @Column(name = "aadhaar_content_type")
    private String aadhaarContentType;

    @Column(name = "resume_file_name")
    private String resumeFileName;

    @Lob
    @Basic(fetch = FetchType.LAZY)
    @Column(name = "resume_file_data", columnDefinition = "LONGBLOB")
    private byte[] resumeFileData;

    @Column(name = "resume_content_type")
    private String resumeContentType;

    @Column(name = "passport_file_name")
    private String passportFileName;

    @Lob
    @Basic(fetch = FetchType.LAZY)
    @Column(name = "passport_file_data", columnDefinition = "LONGBLOB")
    private byte[] passportFileData;

    @Column(name = "passport_content_type")
    private String passportContentType;

    @Column(name = "submitted_at")
    private LocalDateTime submittedAt;

    public DocumentSubmission() {
    }

    public DocumentSubmission(User user) {
        this.user = user;
        this.submittedAt = LocalDateTime.now();
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public String getTenthFileName() {
        return tenthFileName;
    }

    public void setTenthFileName(String tenthFileName) {
        this.tenthFileName = tenthFileName;
    }

    public byte[] getTenthFileData() {
        return tenthFileData;
    }

    public void setTenthFileData(byte[] tenthFileData) {
        this.tenthFileData = tenthFileData;
    }

    public String getTenthContentType() {
        return tenthContentType;
    }

    public void setTenthContentType(String tenthContentType) {
        this.tenthContentType = tenthContentType;
    }

    public String getTwelfthFileName() {
        return twelfthFileName;
    }

    public void setTwelfthFileName(String twelfthFileName) {
        this.twelfthFileName = twelfthFileName;
    }

    public byte[] getTwelfthFileData() {
        return twelfthFileData;
    }

    public void setTwelfthFileData(byte[] twelfthFileData) {
        this.twelfthFileData = twelfthFileData;
    }

    public String getTwelfthContentType() {
        return twelfthContentType;
    }

    public void setTwelfthContentType(String twelfthContentType) {
        this.twelfthContentType = twelfthContentType;
    }

    public String getPcFileName() {
        return pcFileName;
    }

    public void setPcFileName(String pcFileName) {
        this.pcFileName = pcFileName;
    }

    public byte[] getPcFileData() {
        return pcFileData;
    }

    public void setPcFileData(byte[] pcFileData) {
        this.pcFileData = pcFileData;
    }

    public String getPcContentType() {
        return pcContentType;
    }

    public void setPcContentType(String pcContentType) {
        this.pcContentType = pcContentType;
    }

    public String getOdFileName() {
        return odFileName;
    }

    public void setOdFileName(String odFileName) {
        this.odFileName = odFileName;
    }

    public byte[] getOdFileData() {
        return odFileData;
    }

    public void setOdFileData(byte[] odFileData) {
        this.odFileData = odFileData;
    }

    public String getOdContentType() {
        return odContentType;
    }

    public void setOdContentType(String odContentType) {
        this.odContentType = odContentType;
    }

    public String getPanFileName() {
        return panFileName;
    }

    public void setPanFileName(String panFileName) {
        this.panFileName = panFileName;
    }

    public byte[] getPanFileData() {
        return panFileData;
    }

    public void setPanFileData(byte[] panFileData) {
        this.panFileData = panFileData;
    }

    public String getPanContentType() {
        return panContentType;
    }

    public void setPanContentType(String panContentType) {
        this.panContentType = panContentType;
    }

    public String getAadhaarFileName() {
        return aadhaarFileName;
    }

    public void setAadhaarFileName(String aadhaarFileName) {
        this.aadhaarFileName = aadhaarFileName;
    }

    public byte[] getAadhaarFileData() {
        return aadhaarFileData;
    }

    public void setAadhaarFileData(byte[] aadhaarFileData) {
        this.aadhaarFileData = aadhaarFileData;
    }

    public String getAadhaarContentType() {
        return aadhaarContentType;
    }

    public void setAadhaarContentType(String aadhaarContentType) {
        this.aadhaarContentType = aadhaarContentType;
    }

    public String getResumeFileName() {
        return resumeFileName;
    }

    public void setResumeFileName(String resumeFileName) {
        this.resumeFileName = resumeFileName;
    }

    public byte[] getResumeFileData() {
        return resumeFileData;
    }

    public void setResumeFileData(byte[] resumeFileData) {
        this.resumeFileData = resumeFileData;
    }

    public String getResumeContentType() {
        return resumeContentType;
    }

    public void setResumeContentType(String resumeContentType) {
        this.resumeContentType = resumeContentType;
    }

    public String getPassportFileName() {
        return passportFileName;
    }

    public void setPassportFileName(String passportFileName) {
        this.passportFileName = passportFileName;
    }

    public byte[] getPassportFileData() {
        return passportFileData;
    }

    public void setPassportFileData(byte[] passportFileData) {
        this.passportFileData = passportFileData;
    }

    public String getPassportContentType() {
        return passportContentType;
    }

    public void setPassportContentType(String passportContentType) {
        this.passportContentType = passportContentType;
    }

    public LocalDateTime getSubmittedAt() {
        return submittedAt;
    }

    public void setSubmittedAt(LocalDateTime submittedAt) {
        this.submittedAt = submittedAt;
    }
}
