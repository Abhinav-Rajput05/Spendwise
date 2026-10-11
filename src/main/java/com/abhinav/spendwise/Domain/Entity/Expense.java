package com.abhinav.spendwise.Domain.Entity;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.UUID;

import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EntityListeners;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.PrePersist;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@EntityListeners(AuditingEntityListener.class)
@Entity
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Expense {

    @Id
    @GeneratedValue(strategy=GenerationType.UUID)
    private UUID id;

    @ManyToOne(fetch=FetchType.LAZY)
    @JoinColumn(name="user_id")
    private User user;

    @ManyToOne(fetch=FetchType.LAZY)
    @JoinColumn(name="category_id")
    private Category category;

    @Column(nullable = false)
    @Positive(message = "Amount must be positive")
    @NotNull
    private Double amount;

    @Column(columnDefinition = "TEXT")
    private String description;

    @Column(nullable = false)
    private LocalDate date; // date of an expense 

     @CreatedDate
     @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt; // record at created time

    @LastModifiedDate
    private LocalDateTime updatedAt;

   @PrePersist
    public void prePersist() {
        if (date == null) {
            date = LocalDate.now(); // Set to current date if not provided
        }
    }

   @Override
   public int hashCode() {
    final int prime = 31;
    int result = 1;
    result = prime * result + ((id == null) ? 0 : id.hashCode());
    result = prime * result + ((user == null) ? 0 : user.hashCode());
    result = prime * result + ((category == null) ? 0 : category.hashCode());
    result = prime * result + ((amount == null) ? 0 : amount.hashCode());
    result = prime * result + ((description == null) ? 0 : description.hashCode());
    result = prime * result + ((date == null) ? 0 : date.hashCode());
    result = prime * result + ((createdAt == null) ? 0 : createdAt.hashCode());
    result = prime * result + ((updatedAt == null) ? 0 : updatedAt.hashCode());
    return result;
   }

   @Override
   public boolean equals(Object obj) {
    if (this == obj)
        return true;
    if (obj == null)
        return false;
    if (getClass() != obj.getClass())
        return false;
    Expense other = (Expense) obj;
    if (id == null) {
        if (other.id != null)
            return false;
    } else if (!id.equals(other.id))
        return false;
    if (user == null) {
        if (other.user != null)
            return false;
    } else if (!user.equals(other.user))
        return false;
    if (category == null) {
        if (other.category != null)
            return false;
    } else if (!category.equals(other.category))
        return false;
    if (amount == null) {
        if (other.amount != null)
            return false;
    } else if (!amount.equals(other.amount))
        return false;
    if (description == null) {
        if (other.description != null)
            return false;
    } else if (!description.equals(other.description))
        return false;
    if (date == null) {
        if (other.date != null)
            return false;
    } else if (!date.equals(other.date))
        return false;
    if (createdAt == null) {
        if (other.createdAt != null)
            return false;
    } else if (!createdAt.equals(other.createdAt))
        return false;
    if (updatedAt == null) {
        if (other.updatedAt != null)
            return false;
    } else if (!updatedAt.equals(other.updatedAt))
        return false;
    return true;
   }


  
}
