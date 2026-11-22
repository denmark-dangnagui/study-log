package com.example.lab.api;

import java.util.List;

public record TeamResponse(String name, List<String> memberNames) {
}
