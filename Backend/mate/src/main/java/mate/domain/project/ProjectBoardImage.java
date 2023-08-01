package mate.domain.project;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

@Entity
@Table(name = "project_board_image")
public class ProjectBoardImage {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer idx;

	@ManyToOne
	@JoinColumn(name = "board_idx")
	private ProjectBoard projectBoard;

	@Column(name = "image_url")
	private String imageUrl;

	// Getters and setters, constructors, and other methods
	// ...
}
