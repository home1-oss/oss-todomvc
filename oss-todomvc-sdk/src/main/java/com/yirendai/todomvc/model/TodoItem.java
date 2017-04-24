package com.yirendai.todomvc.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TodoItem implements Serializable {

  private Long id;
  private String title;
  private Boolean completed = Boolean.FALSE;
}
