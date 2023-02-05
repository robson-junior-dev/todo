package br.dev.robsonjunior.todo.domain;

import static org.assertj.core.api.Assertions.assertThat;

import br.dev.robsonjunior.todo.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class SprintTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Sprint.class);
        Sprint sprint1 = new Sprint();
        sprint1.setId(1L);
        Sprint sprint2 = new Sprint();
        sprint2.setId(sprint1.getId());
        assertThat(sprint1).isEqualTo(sprint2);
        sprint2.setId(2L);
        assertThat(sprint1).isNotEqualTo(sprint2);
        sprint1.setId(null);
        assertThat(sprint1).isNotEqualTo(sprint2);
    }
}
