package com.baiyi.caesar.domain.generator.caesar;

import java.util.Date;
import javax.persistence.*;

@Table(name = "cs_block_platform")
public class CsBlockPlatform {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    /**
     * 封网级别 P0-P4
     */
    @Column(name = "block_level")
    private String blockLevel;

    /**
     * 标题
     */
    private String title;

    /**
     * 有效
     */
    private Boolean valid;

    @Column(name = "start_time")
    private Date startTime;

    @Column(name = "end_time")
    private Date endTime;

    /**
     * 创建时间
     */
    @Column(name = "create_time")
    private Date createTime;

    /**
     * 更新时间
     */
    @Column(name = "update_time")
    private Date updateTime;

    /**
     * 描述
     */
    private String comment;

    /**
     * @return id
     */
    public Integer getId() {
        return id;
    }

    /**
     * @param id
     */
    public void setId(Integer id) {
        this.id = id;
    }

    /**
     * 获取封网级别 P0-P4
     *
     * @return block_level - 封网级别 P0-P4
     */
    public String getBlockLevel() {
        return blockLevel;
    }

    /**
     * 设置封网级别 P0-P4
     *
     * @param blockLevel 封网级别 P0-P4
     */
    public void setBlockLevel(String blockLevel) {
        this.blockLevel = blockLevel;
    }

    /**
     * 获取标题
     *
     * @return title - 标题
     */
    public String getTitle() {
        return title;
    }

    /**
     * 设置标题
     *
     * @param title 标题
     */
    public void setTitle(String title) {
        this.title = title;
    }

    /**
     * 获取有效
     *
     * @return valid - 有效
     */
    public Boolean getValid() {
        return valid;
    }

    /**
     * 设置有效
     *
     * @param valid 有效
     */
    public void setValid(Boolean valid) {
        this.valid = valid;
    }

    /**
     * @return start_time
     */
    public Date getStartTime() {
        return startTime;
    }

    /**
     * @param startTime
     */
    public void setStartTime(Date startTime) {
        this.startTime = startTime;
    }

    /**
     * @return end_time
     */
    public Date getEndTime() {
        return endTime;
    }

    /**
     * @param endTime
     */
    public void setEndTime(Date endTime) {
        this.endTime = endTime;
    }

    /**
     * 获取创建时间
     *
     * @return create_time - 创建时间
     */
    public Date getCreateTime() {
        return createTime;
    }

    /**
     * 设置创建时间
     *
     * @param createTime 创建时间
     */
    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    /**
     * 获取更新时间
     *
     * @return update_time - 更新时间
     */
    public Date getUpdateTime() {
        return updateTime;
    }

    /**
     * 设置更新时间
     *
     * @param updateTime 更新时间
     */
    public void setUpdateTime(Date updateTime) {
        this.updateTime = updateTime;
    }

    /**
     * 获取描述
     *
     * @return comment - 描述
     */
    public String getComment() {
        return comment;
    }

    /**
     * 设置描述
     *
     * @param comment 描述
     */
    public void setComment(String comment) {
        this.comment = comment;
    }
}