package ru.skillbox.rest_news_service.web.controller.v1;

import net.bytebuddy.utility.RandomString;;
import net.javacrumbs.jsonunit.JsonAssert;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.mockito.Mockito;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.MediaType;
import ru.skillbox.rest_news_service.AbstractTestController;
import ru.skillbox.rest_news_service.StringTestUtils;
import ru.skillbox.rest_news_service.exception.EntityNotFoundException;
import ru.skillbox.rest_news_service.mapper.AuthorMapper;
import ru.skillbox.rest_news_service.model.Author;
import ru.skillbox.rest_news_service.model.Comment;
import ru.skillbox.rest_news_service.model.News;
import ru.skillbox.rest_news_service.service.AuthorService;
import ru.skillbox.rest_news_service.web.model.AuthorListResponse;
import ru.skillbox.rest_news_service.web.model.AuthorResponse;
import ru.skillbox.rest_news_service.web.model.NewsResponse;
import ru.skillbox.rest_news_service.web.model.UpsertAuthorRequest;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


public class AuthorControllerTest extends AbstractTestController {
    @MockBean
    private AuthorService authorService;
    @MockBean
    private AuthorMapper authorMapper;

    @Test
    public void whenFindAll_thenReturnAllAuthors() throws Exception {
        // Подготовка тестовых данных
        List<Author> authors = new ArrayList<>();

        authors.add(createAuthor(1L, null, null));

        News news = createNews(1L, "TestNews", null, null);

        Comment comment = createComment(1L, "TestComment", null);

        authors.add(createAuthor(2L, news, comment));

        List<AuthorResponse> authorResponses = new ArrayList<>();

        authorResponses.add(createAuthorResponse(1L, null));

        NewsResponse newsResponse = createNewsResponse(1L, "TestNewsText");
        authorResponses.add(createAuthorResponse(2L, newsResponse));

        // Создание Page<Author> для имитации ответа от репозитория
        Page<Author> authorPage = new PageImpl<>(authors, PageRequest.of(0, 10), authors.size());

        // Создание AuthorListResponse с использованием конструктора
        AuthorListResponse authorListResponse = new AuthorListResponse(
                authorResponses,
                authorPage.getTotalElements(),
                authorPage.getTotalPages(),
                authorPage.getNumber(),
                authorPage.getSize()
        );

        // Мокирование сервисного слоя
        Mockito.when(authorService.findAll(0, 10)).thenReturn(authorListResponse);
        Mockito.when(authorMapper.authorListToAuthorResponseList(authorPage)).thenReturn(authorListResponse);

        // Выполнение запроса и проверка результатов
        String actualResponse = mockMvc.perform(get("/api/v1/author")
                        .param("page", "0")
                        .param("size", "10"))
                .andExpect(status().isOk())
                .andReturn()
                .getResponse()
                .getContentAsString();

        String expectedResponse = StringTestUtils.readStringFromResource("response/find_all_authors_response.json");

        Mockito.verify(authorService, Mockito.times(1)).findAll(0, 10);
        JsonAssert.assertJsonEquals(expectedResponse, actualResponse);
    }


    @Test
    public void whenGetAuthorById_thenReturnAuthorById() throws Exception {
        Author author = createAuthor(1L, null, null);
        AuthorResponse authorResponse = createAuthorResponse(1L, null);
        Mockito.when(authorService.findById(1L)).thenReturn(authorResponse);
        Mockito.when(authorMapper.authorToResponse(author)).thenReturn(authorResponse);
        String actualResponse = mockMvc.perform(get("/api/v1/author/1")).andExpect(status().isOk()).andReturn().getResponse().getContentAsString();
        String expectedResponse = StringTestUtils.readStringFromResource("response/find_author_by_id_response.json");
        Mockito.verify(authorService, Mockito.times(1)).findById(1L);
        JsonAssert.assertJsonEquals(expectedResponse, actualResponse);
    }

    @Test
    public void whenCreateAuthor_thenReturnNewAuthor() throws Exception {
        Author author = new Author();
        author.setName("Client 1");
        Author createdAuthor = createAuthor(1L, null, null);
        AuthorResponse authorResponse = createAuthorResponse(1L, null);
        UpsertAuthorRequest upsertAuthorRequest = new UpsertAuthorRequest("Author 1", null, null);
        Mockito.when(authorService.save(upsertAuthorRequest)).thenReturn(authorResponse);
        Mockito.when(authorMapper.requestToAuthor(upsertAuthorRequest)).thenReturn(author);
        Mockito.when(authorMapper.authorToResponse(createdAuthor)).thenReturn(authorResponse);
        String actualResponse = mockMvc.perform(post("/api/v1/author").contentType(MediaType.APPLICATION_JSON).content(objectMapper.writeValueAsString(upsertAuthorRequest))).andExpect(status().isCreated()).andReturn().getResponse().getContentAsString();
        String expectedResponse = StringTestUtils.readStringFromResource("response/create_author_response.json");
        Mockito.verify(authorService, Mockito.times(1)).save(upsertAuthorRequest);
        JsonAssert.assertJsonEquals(expectedResponse, actualResponse);
    }

    @Test
    public void whenUpdateAuthor_thenReturnUpdatedAuthor() throws Exception {
        UpsertAuthorRequest request = new UpsertAuthorRequest("New Author 1", null, null);
        Author updatedAuthor = new Author(1L, "New Author 1", new ArrayList<>(), new ArrayList<>());
        AuthorResponse authorResponse = new AuthorResponse(1L, "New Author 1", 2L, 2L);
        Mockito.when(authorService.update(request, 1L)).thenReturn(authorResponse);
        Mockito.when(authorMapper.requestToAuthor(1L, request)).thenReturn(updatedAuthor);
        Mockito.when(authorMapper.authorToResponse(updatedAuthor)).thenReturn(authorResponse);
        String actualResponse = mockMvc.perform(put("/api/v1/author/1").contentType(MediaType.APPLICATION_JSON).content(objectMapper.writeValueAsString(request))).andExpect(status().isOk()).andReturn().getResponse().getContentAsString();
        String expectedResponse = StringTestUtils.readStringFromResource("response/update_author_response.json");
        Mockito.verify(authorService, Mockito.times(1)).update(request, 1L);
        JsonAssert.assertJsonEquals(expectedResponse, actualResponse);
    }

    @Test
    public void whenDeleteAuthorById_thenReturnStatusNoContent() throws Exception {
        mockMvc.perform(delete("/api/v1/author/1"))
                .andExpect(status().isNoContent());

        Mockito.verify(authorService, Mockito.times(1)).deleteById(1L);
    }

    @Test
    public void thenFindNotExistedAuthorThenReturnError() throws Exception {
        Mockito.when(authorService.findById(500L)).thenThrow(new EntityNotFoundException("Автор с ID 500 не найден!"));

        var response = mockMvc.perform(get("/api/v1/author/500")).andExpect(status().isNotFound()).andReturn().getResponse();
        response.setCharacterEncoding("UTF-8");
        String actualResponse = response.getContentAsString();
        String expectedResponse = StringTestUtils.readStringFromResource("response/author_by_id_not_found_response.json");
        Mockito.verify(authorService, Mockito.times(1)).findById(500L);
        JsonAssert.assertJsonEquals(expectedResponse, actualResponse);
    }

    @Test
    public void whenCreateAuthorWithEmptyName_thenReturnError() throws Exception {
        var response = mockMvc.perform(post("/api/v1/author")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(new UpsertAuthorRequest())))
                .andExpect(status()
                        .isBadRequest())
                .andReturn()
                .getResponse();
        response.setCharacterEncoding("UTF-8");
        String actualResponse = response.getContentAsString();
        String expectedResponse = StringTestUtils.readStringFromResource("response/empty_author_name_response.json");
        JsonAssert.assertJsonEquals(expectedResponse, actualResponse);
    }

    @ParameterizedTest
    @MethodSource("invalidSizeName")
    public void whenCreateAuthorWithInvalidName_thenReturnError(String name) throws Exception {
        var response = mockMvc.perform(post("/api/v1/author")
                        .contentType(MediaType.APPLICATION_JSON).content(objectMapper.writeValueAsString(new UpsertAuthorRequest(name, null, null))))
                .andExpect(status().isBadRequest())
                .andReturn()
                .getResponse();
        response.setCharacterEncoding("UTF-8");
        String actualResponse = response.getContentAsString();
        String expectedResponse = StringTestUtils.readStringFromResource("response/author_name_size_exception_response.json");
        JsonAssert.assertJsonEquals(expectedResponse, actualResponse);

    }

    private static Stream<Arguments> invalidSizeName() {
        return Stream.of(Arguments.of(RandomString.make(2)),
                Arguments.of(RandomString.make(31))
        );
    }
}
