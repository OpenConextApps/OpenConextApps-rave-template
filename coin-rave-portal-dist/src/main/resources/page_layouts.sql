set @page_layout_seq = 'page_layout';

set @one_col_id = (SELECT seq_count FROM RAVE_PORTAL_SEQUENCES WHERE seq_name = @page_layout_seq);
insert into page_layout (entity_id, code,  number_of_regions)
values (@one_col_id, 'columns_1', 1);
UPDATE RAVE_PORTAL_SEQUENCES SET seq_count = (seq_count + 1) WHERE seq_name = @page_layout_seq;

set @two_col_id = (SELECT seq_count FROM RAVE_PORTAL_SEQUENCES WHERE seq_name = @page_layout_seq);
insert into page_layout (entity_id, code,  number_of_regions)
values (@two_col_id, 'columns_2', 2);
UPDATE RAVE_PORTAL_SEQUENCES SET seq_count = (seq_count + 1) WHERE seq_name = @page_layout_seq;

set @twown_col_id = (SELECT seq_count FROM RAVE_PORTAL_SEQUENCES WHERE seq_name = @page_layout_seq);
insert into page_layout (entity_id, code,  number_of_regions)
values (@twown_col_id, 'columns_2wn', 2);
UPDATE RAVE_PORTAL_SEQUENCES SET seq_count = (seq_count + 1) WHERE seq_name = @page_layout_seq;

set @three_col_id = (SELECT seq_count FROM RAVE_PORTAL_SEQUENCES WHERE seq_name = @page_layout_seq);
insert into page_layout (entity_id, code,  number_of_regions)
values (@three_col_id, 'columns_3', 3);
UPDATE RAVE_PORTAL_SEQUENCES SET seq_count = (seq_count + 1) WHERE seq_name = @page_layout_seq;

set @threewn_col_id = (SELECT seq_count FROM RAVE_PORTAL_SEQUENCES WHERE seq_name = @page_layout_seq);
insert into page_layout (entity_id, code,  number_of_regions)
values (@threewn_col_id, 'columns_3nwn', 3);
UPDATE RAVE_PORTAL_SEQUENCES SET seq_count = (seq_count + 1) WHERE seq_name = @page_layout_seq;

set @four_col_id = (SELECT seq_count FROM RAVE_PORTAL_SEQUENCES WHERE seq_name = @page_layout_seq);
insert into page_layout (entity_id, code,  number_of_regions)
values (@four_col_id, 'columns_4', 4);
UPDATE RAVE_PORTAL_SEQUENCES SET seq_count = (seq_count + 1) WHERE seq_name = @page_layout_seq;

set @fourwn_col_id = (SELECT seq_count FROM RAVE_PORTAL_SEQUENCES WHERE seq_name = @page_layout_seq);
insert into page_layout (entity_id, code,  number_of_regions)
values (@fourwn_col_id, 'columns_3nwn_1_bottom', 4);
UPDATE RAVE_PORTAL_SEQUENCES SET seq_count = (seq_count + 1) WHERE seq_name = @page_layout_seq;

