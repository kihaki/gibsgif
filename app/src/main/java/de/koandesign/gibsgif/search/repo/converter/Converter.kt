package de.koandesign.gibsgif.search.repo.converter

interface Converter<FROM, TO> : (FROM) -> TO
