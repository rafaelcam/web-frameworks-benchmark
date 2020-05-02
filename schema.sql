CREATE TABLE public.developer
(
    id uuid PRIMARY KEY NOT NULL,
    name varchar(255) NOT NULL,
    age int NOT NULL,
    expertise varchar(255) NOT NULL,
    created_at timestamp NOT NULL
);
CREATE INDEX developer_expertise_index ON public.developer (expertise);