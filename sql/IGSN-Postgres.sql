--
-- PostgreSQL database dump
--

-- Dumped from database version 9.3.5
-- Dumped by pg_dump version 9.5.5

-- Started on 2017-02-24 09:50:31

SET statement_timeout = 0;
SET lock_timeout = 0;
SET client_encoding = 'UTF8';
SET standard_conforming_strings = on;
SET check_function_bodies = false;
SET client_min_messages = warning;


--
-- TOC entry 15 (class 2615 OID 73271)
-- Name: version30; Type: SCHEMA; Schema: -; Owner: -
--

CREATE SCHEMA version30;


SET search_path = version30, pg_catalog;

--
-- TOC entry 320 (class 1259 OID 73599)
-- Name: allocator_allocatorid_seq; Type: SEQUENCE; Schema: version30; Owner: -
--

CREATE SEQUENCE allocator_allocatorid_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


SET default_with_oids = false;

--
-- TOC entry 315 (class 1259 OID 73578)
-- Name: allocator; Type: TABLE; Schema: version30; Owner: -
--

CREATE TABLE allocator (
    allocatorid integer DEFAULT nextval('allocator_allocatorid_seq'::regclass) NOT NULL,
    comments text,
    contactemail character varying(255) NOT NULL,
    contactname character varying(80) NOT NULL,
    created timestamp without time zone,
    username character varying(50) NOT NULL,
    password character varying(50),
    isactive boolean
);


--
-- TOC entry 316 (class 1259 OID 73584)
-- Name: allocator_prefixes; Type: TABLE; Schema: version30; Owner: -
--

CREATE TABLE allocator_prefixes (
    allocator integer NOT NULL,
    prefixes integer NOT NULL
);


--
-- TOC entry 287 (class 1259 OID 73284)
-- Name: alternate_identifiers; Type: TABLE; Schema: version30; Owner: -
--

CREATE TABLE alternate_identifiers (
    resource_identifier character varying,
    alternate_identifier character varying,
    alternate_identifiers_id integer NOT NULL
);


--
-- TOC entry 302 (class 1259 OID 73544)
-- Name: alternate_identifiers_id_seq; Type: SEQUENCE; Schema: version30; Owner: -
--

CREATE SEQUENCE alternate_identifiers_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


--
-- TOC entry 290 (class 1259 OID 73334)
-- Name: classifications; Type: TABLE; Schema: version30; Owner: -
--

CREATE TABLE classifications (
    classifications_id integer NOT NULL,
    resource_identifier character varying,
    classification_uri character varying,
    classification character varying
);


--
-- TOC entry 303 (class 1259 OID 73546)
-- Name: classifications_id_seq; Type: SEQUENCE; Schema: version30; Owner: -
--

CREATE SEQUENCE classifications_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


--
-- TOC entry 304 (class 1259 OID 73548)
-- Name: collector_id_seq; Type: SEQUENCE; Schema: version30; Owner: -
--

CREATE SEQUENCE collector_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


--
-- TOC entry 305 (class 1259 OID 73551)
-- Name: contributor_id_seq; Type: SEQUENCE; Schema: version30; Owner: -
--

CREATE SEQUENCE contributor_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


--
-- TOC entry 294 (class 1259 OID 73399)
-- Name: contributors; Type: TABLE; Schema: version30; Owner: -
--

CREATE TABLE contributors (
    contributor_id integer NOT NULL,
    contributor_type character varying,
    contributor_name character varying,
    contributor_identifier character varying,
    resource_identifier character varying,
    contributor_identifier_type integer
);


--
-- TOC entry 293 (class 1259 OID 73386)
-- Name: curation_details; Type: TABLE; Schema: version30; Owner: -
--

CREATE TABLE curation_details (
    curation_id integer NOT NULL,
    curator character varying,
    curation_location character varying,
    institution_uri character varying,
    resource_identifier character varying,
    curating_institution character varying,
    curation_date character varying
);


--
-- TOC entry 306 (class 1259 OID 73553)
-- Name: curation_id_seq; Type: SEQUENCE; Schema: version30; Owner: -
--

CREATE SEQUENCE curation_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


--
-- TOC entry 300 (class 1259 OID 73483)
-- Name: cv_identifier_type; Type: TABLE; Schema: version30; Owner: -
--

CREATE TABLE cv_identifier_type (
    cv_identifier_type_id integer NOT NULL,
    identifier_type character varying
);


--
-- TOC entry 298 (class 1259 OID 73470)
-- Name: cv_material_types; Type: TABLE; Schema: version30; Owner: -
--

CREATE TABLE cv_material_types (
    cv_material_type_id integer NOT NULL,
    material_type character varying(100)
);


--
-- TOC entry 299 (class 1259 OID 73475)
-- Name: cv_resource_type; Type: TABLE; Schema: version30; Owner: -
--

CREATE TABLE cv_resource_type (
    cv_resource_type_id integer NOT NULL,
    resource_type character varying
);


--
-- TOC entry 292 (class 1259 OID 73360)
-- Name: location; Type: TABLE; Schema: version30; Owner: -
--

CREATE TABLE location (
    location_id integer NOT NULL,
    locality character varying,
    locality_uri character varying,
    srid character varying,
    vertical_datum character varying,
    geometry_uri character varying,
    geometry public.geometry(Geometry,4326)
);


--
-- TOC entry 307 (class 1259 OID 73555)
-- Name: location_id_seq; Type: SEQUENCE; Schema: version30; Owner: -
--

CREATE SEQUENCE location_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


--
-- TOC entry 296 (class 1259 OID 73425)
-- Name: log_date; Type: TABLE; Schema: version30; Owner: -
--

CREATE TABLE log_date (
    log_date_id integer NOT NULL,
    event_type character varying,
    log_date character varying
);


--
-- TOC entry 308 (class 1259 OID 73557)
-- Name: log_date_id_seq; Type: SEQUENCE; Schema: version30; Owner: -
--

CREATE SEQUENCE log_date_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


--
-- TOC entry 289 (class 1259 OID 73321)
-- Name: material_types; Type: TABLE; Schema: version30; Owner: -
--

CREATE TABLE material_types (
    material_types_id integer NOT NULL,
    resource_identifier character varying,
    material_type integer
);


--
-- TOC entry 309 (class 1259 OID 73559)
-- Name: material_types_id_seq; Type: SEQUENCE; Schema: version30; Owner: -
--

CREATE SEQUENCE material_types_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


--
-- TOC entry 297 (class 1259 OID 73442)
-- Name: method; Type: TABLE; Schema: version30; Owner: -
--

CREATE TABLE method (
    method_id integer NOT NULL,
    method character varying,
    method_uri character varying
);


--
-- TOC entry 310 (class 1259 OID 73561)
-- Name: method_id_seq; Type: SEQUENCE; Schema: version30; Owner: -
--

CREATE SEQUENCE method_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


--
-- TOC entry 322 (class 1259 OID 73606)
-- Name: prefix_id_seq; Type: SEQUENCE; Schema: version30; Owner: -
--

CREATE SEQUENCE prefix_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


--
-- TOC entry 317 (class 1259 OID 73587)
-- Name: prefix; Type: TABLE; Schema: version30; Owner: -
--

CREATE TABLE prefix (
    id integer DEFAULT nextval('prefix_id_seq'::regclass) NOT NULL,
    prefix character varying(15) NOT NULL,
    created timestamp without time zone DEFAULT now(),
    version integer,
    description character varying
);


--
-- TOC entry 321 (class 1259 OID 73603)
-- Name: registrant_registrantid_seq; Type: SEQUENCE; Schema: version30; Owner: -
--

CREATE SEQUENCE registrant_registrantid_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


--
-- TOC entry 318 (class 1259 OID 73590)
-- Name: registrant; Type: TABLE; Schema: version30; Owner: -
--

CREATE TABLE registrant (
    registrantid integer DEFAULT nextval('registrant_registrantid_seq'::regclass) NOT NULL,
    registrantname character varying(255) NOT NULL,
    registrantemail character varying(255) NOT NULL,
    created timestamp without time zone,
    username character varying(50) NOT NULL,
    password character varying(50) NOT NULL,
    updated timestamp without time zone,
    allocator integer NOT NULL,
    isactive boolean
);


--
-- TOC entry 319 (class 1259 OID 73596)
-- Name: registrant_prefixes; Type: TABLE; Schema: version30; Owner: -
--

CREATE TABLE registrant_prefixes (
    registrant integer NOT NULL,
    prefixes integer NOT NULL
);


--
-- TOC entry 311 (class 1259 OID 73563)
-- Name: related_resource_id_seq; Type: SEQUENCE; Schema: version30; Owner: -
--

CREATE SEQUENCE related_resource_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


--
-- TOC entry 295 (class 1259 OID 73412)
-- Name: related_resources; Type: TABLE; Schema: version30; Owner: -
--

CREATE TABLE related_resources (
    related_resource_id integer NOT NULL,
    related_resource character varying,
    relation_type character varying,
    resource_identifier character varying,
    related_resource_identifier_type integer
);


--
-- TOC entry 301 (class 1259 OID 73521)
-- Name: resource_date; Type: TABLE; Schema: version30; Owner: -
--

CREATE TABLE resource_date (
    resource_date_id integer NOT NULL,
    time_instant character varying,
    time_period_end character varying,
    time_period_start character varying
);


--
-- TOC entry 312 (class 1259 OID 73565)
-- Name: resource_date_id_seq; Type: SEQUENCE; Schema: version30; Owner: -
--

CREATE SEQUENCE resource_date_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


--
-- TOC entry 313 (class 1259 OID 73567)
-- Name: resource_type_id_seq; Type: SEQUENCE; Schema: version30; Owner: -
--

CREATE SEQUENCE resource_type_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


--
-- TOC entry 288 (class 1259 OID 73308)
-- Name: resource_types; Type: TABLE; Schema: version30; Owner: -
--

CREATE TABLE resource_types (
    resource_type_id integer NOT NULL,
    resource_identifier character varying,
    resource_type integer
);


--
-- TOC entry 286 (class 1259 OID 73276)
-- Name: resources; Type: TABLE; Schema: version30; Owner: -
--

CREATE TABLE resources (
    resource_identifier character varying NOT NULL,
    registered_object_type character varying,
    landing_page character varying,
    is_public boolean,
    resource_title character varying,
    purpose character varying,
    location_id integer,
    campaign character varying,
    comments character varying,
    log_date_id integer,
    method_id integer,
    date integer,
    created timestamp without time zone DEFAULT now(),
    modified timestamp without time zone DEFAULT now(),
    registrantid integer,
    registrant integer,
    resourceid bigint NOT NULL,
    input_method character varying(15),
    embargo_end timestamp without time zone
);


--
-- TOC entry 323 (class 1259 OID 73671)
-- Name: resources_resourceid_seq; Type: SEQUENCE; Schema: version30; Owner: -
--

CREATE SEQUENCE resources_resourceid_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


--
-- TOC entry 3829 (class 0 OID 0)
-- Dependencies: 323
-- Name: resources_resourceid_seq; Type: SEQUENCE OWNED BY; Schema: version30; Owner: -
--

ALTER SEQUENCE resources_resourceid_seq OWNED BY resources.resourceid;


--
-- TOC entry 291 (class 1259 OID 73347)
-- Name: sampled_features; Type: TABLE; Schema: version30; Owner: -
--

CREATE TABLE sampled_features (
    sampled_features_id integer NOT NULL,
    resource_identifier character varying,
    sampled_feature_uri character varying,
    sampled_feature character varying
);


--
-- TOC entry 314 (class 1259 OID 73569)
-- Name: sampled_features_id_seq; Type: SEQUENCE; Schema: version30; Owner: -
--

CREATE SEQUENCE sampled_features_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


--
-- TOC entry 3636 (class 2604 OID 73673)
-- Name: resourceid; Type: DEFAULT; Schema: version30; Owner: -
--

ALTER TABLE ONLY resources ALTER COLUMN resourceid SET DEFAULT nextval('resources_resourceid_seq'::regclass);


--
-- TOC entry 3647 (class 2606 OID 73291)
-- Name: pk_alternate_identifiers; Type: CONSTRAINT; Schema: version30; Owner: -
--

ALTER TABLE ONLY alternate_identifiers
    ADD CONSTRAINT pk_alternate_identifiers PRIMARY KEY (alternate_identifiers_id);


--
-- TOC entry 3653 (class 2606 OID 73341)
-- Name: pk_classifications; Type: CONSTRAINT; Schema: version30; Owner: -
--

ALTER TABLE ONLY classifications
    ADD CONSTRAINT pk_classifications PRIMARY KEY (classifications_id);


--
-- TOC entry 3661 (class 2606 OID 73406)
-- Name: pk_contributors; Type: CONSTRAINT; Schema: version30; Owner: -
--

ALTER TABLE ONLY contributors
    ADD CONSTRAINT pk_contributors PRIMARY KEY (contributor_id);


--
-- TOC entry 3659 (class 2606 OID 73393)
-- Name: pk_curation_details; Type: CONSTRAINT; Schema: version30; Owner: -
--

ALTER TABLE ONLY curation_details
    ADD CONSTRAINT pk_curation_details PRIMARY KEY (curation_id);


--
-- TOC entry 3673 (class 2606 OID 73490)
-- Name: pk_cv_identifier_type; Type: CONSTRAINT; Schema: version30; Owner: -
--

ALTER TABLE ONLY cv_identifier_type
    ADD CONSTRAINT pk_cv_identifier_type PRIMARY KEY (cv_identifier_type_id);


--
-- TOC entry 3669 (class 2606 OID 73474)
-- Name: pk_cv_material_type; Type: CONSTRAINT; Schema: version30; Owner: -
--

ALTER TABLE ONLY cv_material_types
    ADD CONSTRAINT pk_cv_material_type PRIMARY KEY (cv_material_type_id);


--
-- TOC entry 3671 (class 2606 OID 73482)
-- Name: pk_cv_resource_type; Type: CONSTRAINT; Schema: version30; Owner: -
--

ALTER TABLE ONLY cv_resource_type
    ADD CONSTRAINT pk_cv_resource_type PRIMARY KEY (cv_resource_type_id);


--
-- TOC entry 3657 (class 2606 OID 73367)
-- Name: pk_location; Type: CONSTRAINT; Schema: version30; Owner: -
--

ALTER TABLE ONLY location
    ADD CONSTRAINT pk_location PRIMARY KEY (location_id);


--
-- TOC entry 3665 (class 2606 OID 73429)
-- Name: pk_log_date; Type: CONSTRAINT; Schema: version30; Owner: -
--

ALTER TABLE ONLY log_date
    ADD CONSTRAINT pk_log_date PRIMARY KEY (log_date_id);


--
-- TOC entry 3651 (class 2606 OID 73328)
-- Name: pk_material_types; Type: CONSTRAINT; Schema: version30; Owner: -
--

ALTER TABLE ONLY material_types
    ADD CONSTRAINT pk_material_types PRIMARY KEY (material_types_id);


--
-- TOC entry 3667 (class 2606 OID 73449)
-- Name: pk_method; Type: CONSTRAINT; Schema: version30; Owner: -
--

ALTER TABLE ONLY method
    ADD CONSTRAINT pk_method PRIMARY KEY (method_id);


--
-- TOC entry 3663 (class 2606 OID 73436)
-- Name: pk_related_resource; Type: CONSTRAINT; Schema: version30; Owner: -
--

ALTER TABLE ONLY related_resources
    ADD CONSTRAINT pk_related_resource PRIMARY KEY (related_resource_id);


--
-- TOC entry 3644 (class 2606 OID 73283)
-- Name: pk_resource; Type: CONSTRAINT; Schema: version30; Owner: -
--

ALTER TABLE ONLY resources
    ADD CONSTRAINT pk_resource PRIMARY KEY (resource_identifier);


--
-- TOC entry 3675 (class 2606 OID 73525)
-- Name: pk_resource_date; Type: CONSTRAINT; Schema: version30; Owner: -
--

ALTER TABLE ONLY resource_date
    ADD CONSTRAINT pk_resource_date PRIMARY KEY (resource_date_id);


--
-- TOC entry 3649 (class 2606 OID 73315)
-- Name: pk_resources; Type: CONSTRAINT; Schema: version30; Owner: -
--

ALTER TABLE ONLY resource_types
    ADD CONSTRAINT pk_resources PRIMARY KEY (resource_type_id);


--
-- TOC entry 3655 (class 2606 OID 73354)
-- Name: pk_sampled_features; Type: CONSTRAINT; Schema: version30; Owner: -
--

ALTER TABLE ONLY sampled_features
    ADD CONSTRAINT pk_sampled_features PRIMARY KEY (sampled_features_id);


--
-- TOC entry 3677 (class 2606 OID 73612)
-- Name: pk_v30_allocatorid; Type: CONSTRAINT; Schema: version30; Owner: -
--

ALTER TABLE ONLY allocator
    ADD CONSTRAINT pk_v30_allocatorid PRIMARY KEY (allocatorid);


--
-- TOC entry 3681 (class 2606 OID 73619)
-- Name: pk_v30_prefix; Type: CONSTRAINT; Schema: version30; Owner: -
--

ALTER TABLE ONLY prefix
    ADD CONSTRAINT pk_v30_prefix PRIMARY KEY (id);


--
-- TOC entry 3685 (class 2606 OID 73631)
-- Name: pk_v30_registrant; Type: CONSTRAINT; Schema: version30; Owner: -
--

ALTER TABLE ONLY registrant
    ADD CONSTRAINT pk_v30_registrant PRIMARY KEY (registrantid);


--
-- TOC entry 3683 (class 2606 OID 73770)
-- Name: prefix_prefix_unique; Type: CONSTRAINT; Schema: version30; Owner: -
--

ALTER TABLE ONLY prefix
    ADD CONSTRAINT prefix_prefix_unique UNIQUE (prefix);


--
-- TOC entry 3679 (class 2606 OID 73610)
-- Name: v30_allocator_prefixes_primary; Type: CONSTRAINT; Schema: version30; Owner: -
--

ALTER TABLE ONLY allocator_prefixes
    ADD CONSTRAINT v30_allocator_prefixes_primary PRIMARY KEY (allocator, prefixes);


--
-- TOC entry 3687 (class 2606 OID 73633)
-- Name: v30_registrant_prefixes_primary; Type: CONSTRAINT; Schema: version30; Owner: -
--

ALTER TABLE ONLY registrant_prefixes
    ADD CONSTRAINT v30_registrant_prefixes_primary PRIMARY KEY (registrant, prefixes);


--
-- TOC entry 3645 (class 1259 OID 73307)
-- Name: fki_resources; Type: INDEX; Schema: version30; Owner: -
--

CREATE INDEX fki_resources ON alternate_identifiers USING btree (resource_identifier);


--
-- TOC entry 3694 (class 2606 OID 73302)
-- Name: fk_alternate_identifiers_resources; Type: FK CONSTRAINT; Schema: version30; Owner: -
--

ALTER TABLE ONLY alternate_identifiers
    ADD CONSTRAINT fk_alternate_identifiers_resources FOREIGN KEY (resource_identifier) REFERENCES resources(resource_identifier) ON DELETE CASCADE;


--
-- TOC entry 3688 (class 2606 OID 73663)
-- Name: fk_c3p8dx0u16gejn29tucchpfl7; Type: FK CONSTRAINT; Schema: version30; Owner: -
--

ALTER TABLE ONLY resources
    ADD CONSTRAINT fk_c3p8dx0u16gejn29tucchpfl7 FOREIGN KEY (registrant) REFERENCES registrant(registrantid);


--
-- TOC entry 3699 (class 2606 OID 73342)
-- Name: fk_classifications_resources; Type: FK CONSTRAINT; Schema: version30; Owner: -
--

ALTER TABLE ONLY classifications
    ADD CONSTRAINT fk_classifications_resources FOREIGN KEY (resource_identifier) REFERENCES resources(resource_identifier) ON DELETE CASCADE;


--
-- TOC entry 3703 (class 2606 OID 73511)
-- Name: fk_contributors_identifier_type; Type: FK CONSTRAINT; Schema: version30; Owner: -
--

ALTER TABLE ONLY contributors
    ADD CONSTRAINT fk_contributors_identifier_type FOREIGN KEY (contributor_identifier_type) REFERENCES cv_identifier_type(cv_identifier_type_id);


--
-- TOC entry 3702 (class 2606 OID 73407)
-- Name: fk_contributors_resources; Type: FK CONSTRAINT; Schema: version30; Owner: -
--

ALTER TABLE ONLY contributors
    ADD CONSTRAINT fk_contributors_resources FOREIGN KEY (resource_identifier) REFERENCES resources(resource_identifier) ON DELETE CASCADE;


--
-- TOC entry 3701 (class 2606 OID 73394)
-- Name: fk_curation_details_resources; Type: FK CONSTRAINT; Schema: version30; Owner: -
--

ALTER TABLE ONLY curation_details
    ADD CONSTRAINT fk_curation_details_resources FOREIGN KEY (resource_identifier) REFERENCES resources(resource_identifier) ON DELETE CASCADE;


--
-- TOC entry 3698 (class 2606 OID 73491)
-- Name: fk_cv_material_type; Type: FK CONSTRAINT; Schema: version30; Owner: -
--

ALTER TABLE ONLY material_types
    ADD CONSTRAINT fk_cv_material_type FOREIGN KEY (material_type) REFERENCES cv_material_types(cv_material_type_id);


--
-- TOC entry 3696 (class 2606 OID 73496)
-- Name: fk_cv_resource_type; Type: FK CONSTRAINT; Schema: version30; Owner: -
--

ALTER TABLE ONLY resource_types
    ADD CONSTRAINT fk_cv_resource_type FOREIGN KEY (resource_type) REFERENCES cv_resource_type(cv_resource_type_id);


--
-- TOC entry 3690 (class 2606 OID 73539)
-- Name: fk_date; Type: FK CONSTRAINT; Schema: version30; Owner: -
--

ALTER TABLE ONLY resources
    ADD CONSTRAINT fk_date FOREIGN KEY (date) REFERENCES resource_date(resource_date_id) ON DELETE CASCADE;


--
-- TOC entry 3693 (class 2606 OID 73368)
-- Name: fk_location; Type: FK CONSTRAINT; Schema: version30; Owner: -
--

ALTER TABLE ONLY resources
    ADD CONSTRAINT fk_location FOREIGN KEY (location_id) REFERENCES location(location_id) ON DELETE CASCADE;


--
-- TOC entry 3692 (class 2606 OID 73430)
-- Name: fk_log_date; Type: FK CONSTRAINT; Schema: version30; Owner: -
--

ALTER TABLE ONLY resources
    ADD CONSTRAINT fk_log_date FOREIGN KEY (log_date_id) REFERENCES log_date(log_date_id) ON DELETE CASCADE;


--
-- TOC entry 3697 (class 2606 OID 73329)
-- Name: fk_material_types_resources; Type: FK CONSTRAINT; Schema: version30; Owner: -
--

ALTER TABLE ONLY material_types
    ADD CONSTRAINT fk_material_types_resources FOREIGN KEY (resource_identifier) REFERENCES resources(resource_identifier) ON DELETE CASCADE;


--
-- TOC entry 3691 (class 2606 OID 73450)
-- Name: fk_method; Type: FK CONSTRAINT; Schema: version30; Owner: -
--

ALTER TABLE ONLY resources
    ADD CONSTRAINT fk_method FOREIGN KEY (method_id) REFERENCES method(method_id) ON DELETE CASCADE;


--
-- TOC entry 3708 (class 2606 OID 73684)
-- Name: fk_registrant_allocator; Type: FK CONSTRAINT; Schema: version30; Owner: -
--

ALTER TABLE ONLY registrant
    ADD CONSTRAINT fk_registrant_allocator FOREIGN KEY (allocator) REFERENCES allocator(allocatorid);


--
-- TOC entry 3705 (class 2606 OID 73506)
-- Name: fk_related_resource_identifier_type; Type: FK CONSTRAINT; Schema: version30; Owner: -
--

ALTER TABLE ONLY related_resources
    ADD CONSTRAINT fk_related_resource_identifier_type FOREIGN KEY (related_resource_identifier_type) REFERENCES cv_identifier_type(cv_identifier_type_id);


--
-- TOC entry 3704 (class 2606 OID 73437)
-- Name: fk_related_resources_resources; Type: FK CONSTRAINT; Schema: version30; Owner: -
--

ALTER TABLE ONLY related_resources
    ADD CONSTRAINT fk_related_resources_resources FOREIGN KEY (resource_identifier) REFERENCES resources(resource_identifier) ON DELETE CASCADE;


--
-- TOC entry 3695 (class 2606 OID 73316)
-- Name: fk_resource_type_resources; Type: FK CONSTRAINT; Schema: version30; Owner: -
--

ALTER TABLE ONLY resource_types
    ADD CONSTRAINT fk_resource_type_resources FOREIGN KEY (resource_identifier) REFERENCES resources(resource_identifier) ON DELETE CASCADE;


--
-- TOC entry 3689 (class 2606 OID 73649)
-- Name: fk_resources_registrant; Type: FK CONSTRAINT; Schema: version30; Owner: -
--

ALTER TABLE ONLY resources
    ADD CONSTRAINT fk_resources_registrant FOREIGN KEY (registrantid) REFERENCES registrant(registrantid);


--
-- TOC entry 3700 (class 2606 OID 73355)
-- Name: fk_sampled_features_resources; Type: FK CONSTRAINT; Schema: version30; Owner: -
--

ALTER TABLE ONLY sampled_features
    ADD CONSTRAINT fk_sampled_features_resources FOREIGN KEY (resource_identifier) REFERENCES resources(resource_identifier) ON DELETE CASCADE;


--
-- TOC entry 3707 (class 2606 OID 73620)
-- Name: fk_v30_allocator; Type: FK CONSTRAINT; Schema: version30; Owner: -
--

ALTER TABLE ONLY allocator_prefixes
    ADD CONSTRAINT fk_v30_allocator FOREIGN KEY (allocator) REFERENCES allocator(allocatorid);


--
-- TOC entry 3706 (class 2606 OID 73625)
-- Name: fk_v30_allocator_prefixes; Type: FK CONSTRAINT; Schema: version30; Owner: -
--

ALTER TABLE ONLY allocator_prefixes
    ADD CONSTRAINT fk_v30_allocator_prefixes FOREIGN KEY (prefixes) REFERENCES prefix(id);


--
-- TOC entry 3709 (class 2606 OID 73639)
-- Name: v30_registrant_prefix; Type: FK CONSTRAINT; Schema: version30; Owner: -
--

ALTER TABLE ONLY registrant_prefixes
    ADD CONSTRAINT v30_registrant_prefix FOREIGN KEY (prefixes) REFERENCES prefix(id);


--
-- TOC entry 3710 (class 2606 OID 73634)
-- Name: v30_registrant_registrant; Type: FK CONSTRAINT; Schema: version30; Owner: -
--

ALTER TABLE ONLY registrant_prefixes
    ADD CONSTRAINT v30_registrant_registrant FOREIGN KEY (registrant) REFERENCES registrant(registrantid);


	
	--
-- TOC entry 3737 (class 0 OID 73483)
-- Dependencies: 300
-- Data for Name: cv_identifier_type; Type: TABLE DATA; Schema: version30; Owner: igsn
--

INSERT INTO cv_identifier_type VALUES (1, 'http://pid.geoscience.gov.au/def/voc/igsn-codelists/ARK');
INSERT INTO cv_identifier_type VALUES (2, 'http://pid.geoscience.gov.au/def/voc/igsn-codelists/arXiv');
INSERT INTO cv_identifier_type VALUES (3, 'http://pid.geoscience.gov.au/def/voc/igsn-codelists/bibcode');
INSERT INTO cv_identifier_type VALUES (4, 'http://pid.geoscience.gov.au/def/voc/igsn-codelists/DOI');
INSERT INTO cv_identifier_type VALUES (5, 'http://pid.geoscience.gov.au/def/voc/igsn-codelists/EAN13');
INSERT INTO cv_identifier_type VALUES (6, 'http://pid.geoscience.gov.au/def/voc/igsn-codelists/EISSN');
INSERT INTO cv_identifier_type VALUES (7, 'http://pid.geoscience.gov.au/def/voc/igsn-codelists/Handle');
INSERT INTO cv_identifier_type VALUES (8, 'http://pid.geoscience.gov.au/def/voc/igsn-codelists/IGSN');
INSERT INTO cv_identifier_type VALUES (9, 'http://pid.geoscience.gov.au/def/voc/igsn-codelists/ISBN');
INSERT INTO cv_identifier_type VALUES (10, 'http://pid.geoscience.gov.au/def/voc/igsn-codelists/ISNI');
INSERT INTO cv_identifier_type VALUES (11, 'http://pid.geoscience.gov.au/def/voc/igsn-codelists/ISSN');
INSERT INTO cv_identifier_type VALUES (12, 'http://pid.geoscience.gov.au/def/voc/igsn-codelists/ISTC');
INSERT INTO cv_identifier_type VALUES (13, 'http://pid.geoscience.gov.au/def/voc/igsn-codelists/LISSN');
INSERT INTO cv_identifier_type VALUES (14, 'http://pid.geoscience.gov.au/def/voc/igsn-codelists/LSID');
INSERT INTO cv_identifier_type VALUES (15, 'http://pid.geoscience.gov.au/def/voc/igsn-codelists/ORCID');
INSERT INTO cv_identifier_type VALUES (16, 'http://pid.geoscience.gov.au/def/voc/igsn-codelists/PMID');
INSERT INTO cv_identifier_type VALUES (17, 'http://pid.geoscience.gov.au/def/voc/igsn-codelists/PURL');
INSERT INTO cv_identifier_type VALUES (18, 'http://pid.geoscience.gov.au/def/voc/igsn-codelists/UPC');
INSERT INTO cv_identifier_type VALUES (19, 'http://pid.geoscience.gov.au/def/voc/igsn-codelists/URL');
INSERT INTO cv_identifier_type VALUES (20, 'http://pid.geoscience.gov.au/def/voc/igsn-codelists/URN');
INSERT INTO cv_identifier_type VALUES (21, 'http://pid.geoscience.gov.au/def/voc/igsn-codelists/VIAF');


--
-- TOC entry 3735 (class 0 OID 73470)
-- Dependencies: 298
-- Data for Name: cv_material_types; Type: TABLE DATA; Schema: version30; Owner: igsn
--

INSERT INTO cv_material_types VALUES (1, 'http://vocabulary.odm2.org/medium/air');
INSERT INTO cv_material_types VALUES (2, 'http://vocabulary.odm2.org/medium/gas');
INSERT INTO cv_material_types VALUES (3, 'http://vocabulary.odm2.org/medium/habitat');
INSERT INTO cv_material_types VALUES (4, 'http://vocabulary.odm2.org/medium/ice');
INSERT INTO cv_material_types VALUES (5, 'http://vocabulary.odm2.org/medium/liquidAqueous');
INSERT INTO cv_material_types VALUES (6, 'http://vocabulary.odm2.org/medium/liquidOrganic');
INSERT INTO cv_material_types VALUES (7, 'http://vocabulary.odm2.org/medium/mineral');
INSERT INTO cv_material_types VALUES (8, 'http://vocabulary.odm2.org/medium/notApplicable');
INSERT INTO cv_material_types VALUES (9, 'http://vocabulary.odm2.org/medium/organism');
INSERT INTO cv_material_types VALUES (10, 'http://vocabulary.odm2.org/medium/other');
INSERT INTO cv_material_types VALUES (11, 'http://vocabulary.odm2.org/medium/particulate');
INSERT INTO cv_material_types VALUES (12, 'http://vocabulary.odm2.org/medium/regolith');
INSERT INTO cv_material_types VALUES (13, 'http://vocabulary.odm2.org/medium/rock');
INSERT INTO cv_material_types VALUES (14, 'http://vocabulary.odm2.org/medium/sediment');
INSERT INTO cv_material_types VALUES (15, 'http://vocabulary.odm2.org/medium/snow');
INSERT INTO cv_material_types VALUES (16, 'http://vocabulary.odm2.org/medium/soil');
INSERT INTO cv_material_types VALUES (17, 'http://vocabulary.odm2.org/medium/tissue');
INSERT INTO cv_material_types VALUES (18, 'http://www.opengis.net/def/nil/OGC/0/inapplicable');
INSERT INTO cv_material_types VALUES (19, 'http://www.opengis.net/def/nil/OGC/0/missing');
INSERT INTO cv_material_types VALUES (20, 'http://www.opengis.net/def/nil/OGC/0/template');
INSERT INTO cv_material_types VALUES (21, 'http://www.opengis.net/def/nil/OGC/0/unknown');
INSERT INTO cv_material_types VALUES (22, 'http://www.opengis.net/def/nil/OGC/0/withheld');


--
-- TOC entry 3736 (class 0 OID 73475)
-- Dependencies: 299
-- Data for Name: cv_resource_type; Type: TABLE DATA; Schema: version30; Owner: igsn
--

INSERT INTO cv_resource_type VALUES (30, 'http://www.opengis.net/def/nil/OGC/0/withheld');
INSERT INTO cv_resource_type VALUES (29, 'http://www.opengis.net/def/nil/OGC/0/unknown');
INSERT INTO cv_resource_type VALUES (28, 'http://www.opengis.net/def/nil/OGC/0/template');
INSERT INTO cv_resource_type VALUES (27, 'http://www.opengis.net/def/nil/OGC/0/missing');
INSERT INTO cv_resource_type VALUES (26, 'http://www.opengis.net/def/nil/OGC/0/inapplicable');
INSERT INTO cv_resource_type VALUES (25, 'http://vocabulary.odm2.org/specimentype/thinSection');
INSERT INTO cv_resource_type VALUES (24, 'http://vocabulary.odm2.org/specimentype/terrestrialSection');
INSERT INTO cv_resource_type VALUES (23, 'http://vocabulary.odm2.org/specimentype/standardReferenceSpecimen');
INSERT INTO cv_resource_type VALUES (22, 'http://vocabulary.odm2.org/specimentype/rockPowder');
INSERT INTO cv_resource_type VALUES (21, 'http://vocabulary.odm2.org/specimentype/precipitationBulk');
INSERT INTO cv_resource_type VALUES (20, 'http://vocabulary.odm2.org/specimentype/petriDishDryDeposition');
INSERT INTO cv_resource_type VALUES (19, 'http://vocabulary.odm2.org/specimentype/other');
INSERT INTO cv_resource_type VALUES (18, 'http://vocabulary.odm2.org/specimentype/orientedCore');
INSERT INTO cv_resource_type VALUES (17, 'http://vocabulary.odm2.org/specimentype/litterFallDigestion');
INSERT INTO cv_resource_type VALUES (16, 'http://vocabulary.odm2.org/specimentype/individualSample');
INSERT INTO cv_resource_type VALUES (15, 'http://vocabulary.odm2.org/specimentype/grab');
INSERT INTO cv_resource_type VALUES (14, 'http://vocabulary.odm2.org/specimentype/forestFloorDigestion');
INSERT INTO cv_resource_type VALUES (13, 'http://vocabulary.odm2.org/specimentype/foliageLeaching');
INSERT INTO cv_resource_type VALUES (12, 'http://vocabulary.odm2.org/specimentype/foliageDigestion');
INSERT INTO cv_resource_type VALUES (11, 'http://vocabulary.odm2.org/specimentype/dredge');
INSERT INTO cv_resource_type VALUES (10, 'http://vocabulary.odm2.org/specimentype/cuttings');
INSERT INTO cv_resource_type VALUES (9, 'http://vocabulary.odm2.org/specimentype/coreWholeRound');
INSERT INTO cv_resource_type VALUES (8, 'http://vocabulary.odm2.org/specimentype/coreSub-Piece');
INSERT INTO cv_resource_type VALUES (7, 'http://vocabulary.odm2.org/specimentype/coreSectionHalf');
INSERT INTO cv_resource_type VALUES (6, 'http://vocabulary.odm2.org/specimentype/coreSection');
INSERT INTO cv_resource_type VALUES (5, 'http://vocabulary.odm2.org/specimentype/coreQuarterRound');
INSERT INTO cv_resource_type VALUES (4, 'http://vocabulary.odm2.org/specimentype/corePiece');
INSERT INTO cv_resource_type VALUES (3, 'http://vocabulary.odm2.org/specimentype/coreHalfRound');
INSERT INTO cv_resource_type VALUES (2, 'http://vocabulary.odm2.org/specimentype/core');
INSERT INTO cv_resource_type VALUES (1, 'http://vocabulary.odm2.org/specimentype/automated');
-- Completed on 2017-02-24 09:50:46

--
-- PostgreSQL database dump complete
--

