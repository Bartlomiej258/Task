package com.crud.tasks;

import com.crud.tasks.domain.*;
import com.crud.tasks.mapper.TrelloMapper;
import com.crud.tasks.service.TrelloService;
import com.crud.tasks.trello.facade.TrelloFacade;
import com.crud.tasks.trello.validator.TrelloValidator;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class TrelloFacadeTest {
    @InjectMocks
    private TrelloFacade trelloFacade;
    @Mock
    private TrelloService trelloService;
    @Mock
    private TrelloValidator trelloValidator;
    @Mock
    private TrelloMapper trelloMapper;
    @Mock
    private CreatedTrelloCardDto createdTrelloCardDto;
    @Mock
    private TrelloCardDto trelloCardDto;
    @Mock
    private TrelloCard trelloCard;

    @Test
    void shouldFetchEmptyList() {

        // Given
        List<TrelloListDto> trelloListsDto =
                List.of(new TrelloListDto("1", "test_list", true));

        List<TrelloBoardDto> trelloBoardsDto =
                List.of(new TrelloBoardDto("1", "test1", trelloListsDto));

        List<TrelloList> mappedTrelloLists =
                List.of(new TrelloList("1", "test_list", true));

        List<TrelloBoard> mappedTrelloBoards =
                List.of(new TrelloBoard("1", "test1", mappedTrelloLists));

        when(trelloService.fetchTrelloBoards()).thenReturn(trelloBoardsDto);
        when(trelloMapper.mapToBoards(trelloBoardsDto)).thenReturn(mappedTrelloBoards);
        when(trelloMapper.mapToBoardsDto(anyList())).thenReturn(List.of());
        when(trelloValidator.validateTrelloBoards(mappedTrelloBoards)).thenReturn(List.of());

        List<TrelloBoardDto> trelloBoardDtos = trelloFacade.fetchTrelloBoards();

        assertNotNull(trelloBoardDtos);
        assertEquals(0, trelloBoardDtos.size());

    }

    @Test
    void shouldFetchTrelloBoards() {
        List<TrelloListDto> trelloListsDto =
                List.of(new TrelloListDto("1", "test_list", false));

        List<TrelloBoardDto> trelloBoardsDto =
                List.of(new TrelloBoardDto("1", "test", trelloListsDto));

        List<TrelloList> mappedTrelloLists =
                List.of(new TrelloList("1", "test_list", false));

        List<TrelloBoard> mappedTrelloBoards =
                List.of(new TrelloBoard("1", "test", mappedTrelloLists));

        when(trelloService.fetchTrelloBoards()).thenReturn(trelloBoardsDto);
        when(trelloMapper.mapToBoards(trelloBoardsDto)).thenReturn(mappedTrelloBoards);
        when(trelloMapper.mapToBoardsDto(anyList())).thenReturn(trelloBoardsDto);
        when(trelloValidator.validateTrelloBoards(mappedTrelloBoards)).thenReturn(mappedTrelloBoards);

        List<TrelloBoardDto> trelloBoardDtos = trelloFacade.fetchTrelloBoards();

        assertNotNull(trelloBoardDtos);
        assertEquals(1, trelloBoardDtos.size());

        trelloBoardDtos.forEach(trelloBoardDto -> {
            assertEquals("1", trelloBoardDto.getId());
            assertEquals("test", trelloBoardDto.getName());

            trelloBoardDto.getList().forEach(trelloListDto -> {
                assertEquals("1", trelloListDto.getId());
                assertEquals("test_list", trelloListDto.getName());
                assertFalse(trelloListDto.isClosed());
            });
        });
    }

    

    @Test
    void shouldCreatedTrelloCardDto() {

        //Given
        TrelloBadgesDto trelloBudgetDto = new TrelloBadgesDto(2, new AttachmentByTypeDto(new Trello(2, 3)));
        CreatedTrelloCardDto createdTrelloCardDto = new CreatedTrelloCardDto("1", "card", "example.com", trelloBudgetDto);

        when(trelloMapper.mapToCard(trelloCardDto)).thenReturn(trelloCard);
        when(trelloMapper.mapToCardDto(any())).thenReturn(trelloCardDto);
        when(trelloService.createTrelloCard(trelloCardDto)).thenReturn(createdTrelloCardDto);

        //When
        CreatedTrelloCardDto createdTrelloCardDtos = trelloFacade.createdTrelloCard(trelloCardDto);

        //Then
        assertEquals("1", createdTrelloCardDto.getId());
        assertEquals("example.com", createdTrelloCardDto.getShortUrl());
        assertEquals(3, createdTrelloCardDto.getBadges().getAttachments().getTrello().getCard());
    }

}
